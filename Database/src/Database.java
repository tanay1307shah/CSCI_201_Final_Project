import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {
	Connection conn;
	java.sql.Statement st;
	;
	ResultSet generalResultSet = null;
	ResultSet friendResultSet;
	ResultSet chatResultSet;
	
	String fname;
	String lname;
	List<Integer> friends = new ArrayList<>();
	List<String> friendImages = new ArrayList<>();
	List<String> FriendNames = new ArrayList<>();
	
	public Database(String fname,String lname) {
		try {
			this.fname = fname;
			this.lname = lname;
			
			Class.forName("com.mysql.jdbc.Driver");
			this.conn = DriverManager.getConnection("jdbc:mysql://localhost/MUSICRT?user=root&password=tShah0713!&useSSL=false");
			this.st = conn.createStatement();
			
			PreparedStatement ps = null;
			ps = conn.prepareStatement("SELECT * FROM USERS WHERE fname=? AND lname=?");
			ps.setString(1,this.fname);
			ps.setString(2, this.lname);
			
			this.generalResultSet = ps.executeQuery();
			this.generalResultSet.next();
			
			//File f = new File("pic.jpg");
			PreparedStatement ps1 = null;
			ps1 = conn.prepareStatement("SELECT * FROM Friendship WHERE fromUserId=?");
			ps1.setInt(1, generalResultSet.getInt("userId"));
			this.friendResultSet = ps1.executeQuery();
			
			
			//Storing all the friends ids
			while(this.friendResultSet.next()) {
				int id = this.friendResultSet.getInt("toUserID");
				this.friends.add(id);
			}
			
			//getting all the data of friends from ids
			fillFriendData();
			
			
			PreparedStatement ps2 = null;
			ps2 = conn.prepareStatement("SELECT * FROM Chats WHERE fromUserId=?");
			ps2.setInt(1, generalResultSet.getInt("userId"));
			this.chatResultSet = ps2.executeQuery();
			this.friendResultSet.next();
			
			
		} catch (ClassNotFoundException e) {
			System.out.println("JDBC Driver class: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("SQLE: " + e.getMessage());
		}
	}
	
	public List<String> getFriendImages() {
		return friendImages;
	}

	public void setFriendImages(List<String> friendImages) {
		this.friendImages = friendImages;
	}

	public List<String> getFriendNames() {
		return FriendNames;
	}

	public void setFriendNames(List<String> friendNames) {
		FriendNames = friendNames;
	}

	public ResultSet getGeneralResultSet() {
		return generalResultSet;
	}

	public void setGeneralResultSet(ResultSet generalResultSet) {
		this.generalResultSet = generalResultSet;
	}

	public String songLocation() { 
		String loc = null;
		try {
			loc = generalResultSet.getString("slocation");
		} catch (SQLException e) {
			if(loc == null) {
				return null;
			}
			System.out.println("getSonglocation error: " + e.getMessage());
		}
		return loc;
	}
	
	public boolean getPalttinumStatus() {
		boolean status = false;
		try {
			status  = generalResultSet.getBoolean("plattinumUser");
		} catch (SQLException e) {
			status = false;
			System.out.println("getPlattinumStatus error: " + e.getMessage());
		}
		return status;
	}
	
	public String getProfileImg() {
		String loc = "";
		try {
			loc = generalResultSet.getString("imgLocation");
		}
		catch(SQLException e) {
			System.out.println("Profile Img error: " + e.getMessage());
		}
		return loc;
	}
	
	public void setProfileImg(String loc) {
		PreparedStatement ps = null;
		int userId;
		try {
			
			userId = generalResultSet.getInt("userId");
			ps  = conn.prepareStatement("INSERT INTO USERS(userId,imgLocation) VALUES (?,?)");
			ps.setInt(1, userId);
			ps.setString(2, loc);
			ResultSet rs = ps.executeQuery();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
		}
	}
	
	
	public void fillFriendData() {
		try {	
			for(int i=0; i < this.friends.size();i++) {
				PreparedStatement ps = null;
				ps = conn.prepareStatement("SELECT * FROM USERS"
						+ "WHERE Users.userId = ?");
				ps.setInt(1, this.friends.get(i));
				ResultSet rs =  ps.executeQuery();
				rs.next();
				this.FriendNames.add(rs.getString("fname") + "," + rs.getString("lname"));
				this.friendImages.add(rs.getString("imgLocation"));
			}	
		}catch(SQLException e) {
			
		}
	}
	
	
	
	
	
}
