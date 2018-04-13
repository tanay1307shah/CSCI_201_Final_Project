import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {
	static Connection conn;
	
	/*String fname;
	String lname;
*/	/*List<Integer> friends = new ArrayList<>();
	List<String> friendImages = new ArrayList<>();
	List<String> FriendNames = new ArrayList<>();
	*/
	public Database() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.conn = DriverManager.getConnection("jdbc:mysql://localhost/MUSICRT?user=root&password=tShah0713!&useSSL=false");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	//Return -1 if login is wrong
	public static int login(String username, String password){
		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement("SELECT * FROM USERS WHERE username=? AND pwd=?");
			ps.setString(1,username);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if(rs == null) {
				return -1;
			}
			else {
				return rs.getInt("userId");
			}
		}catch(SQLException e) {
			System.out.println("SQLE in login: " + e.getMessage());
			return -1;
		}
	}
	 
	public static ResultSet getUserData(int id){
		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement("SELECT * FROM USERS WHERE userId=?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			return rs;
		}catch(SQLException e) {
			System.out.println("SQLE in getting user Data: "+ e.getMessage());
			return null;
		}
	}
	
	public static List<Integer> getFriendsIds(int id){
		List<Integer> temp = new ArrayList<>();
		try {
			PreparedStatement ps1 = null;
			ps1 = conn.prepareStatement("SELECT * FROM Friendship WHERE fromUserId=?");
			ps1.setInt(1, id);
			ResultSet rs = ps1.executeQuery();
		
			while(rs.next()) {
				int tid = rs.getInt("toUserID");
				temp.add(id);
			}
			
		}catch(SQLException e) {
			System.out.println("SQLE in getting all friends IDs: " + e.getMessage());
		}
		return temp;
	}
	
	public static List<String> getfriendsName(int id){
		List<Integer> tempId = getFriendsIds(id); 
		List<String> tempName = new ArrayList<>();
		
		for(int i=0; i < tempId.size();i++) {
			try {
				PreparedStatement ps = null;
				ps = conn.prepareStatement("SELECT * FROM USERS"
						+ "WHERE Users.userId = ?");
				ps.setInt(1, tempId.get(i));
				ResultSet rs =  ps.executeQuery();
				rs.next();
				tempName.add(rs.getString("fname") + "," + rs.getString("lname"));
			}catch(SQLException e) {
				System.out.println("SQLE in get friend Name for id " + id + ": " + e.getMessage());
			}
		}
		return tempName;
	}
	
	// -1 means no lobby exsist of that name
	public static int isLobby(String name) {
		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement("SELECT * FROM Lobbies L WHERE Lobbies.lobbyName = ?");
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			if(rs == null) {
				return -1;
			}
			else {
				return rs.getInt("lobbyId");
			}
		}catch(SQLException e) {
			System.out.println("SQLE in isLobby: " + e.getMessage());
			return -1;
			
		}
	}
	
	public static ResultSet getLobby(int lobbyId) {
		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement("SELECT * FROM Lobbies WHERE Lobbies.lobbyId = ?");
			ps.setInt(1, lobbyId);
			ResultSet rs = ps.executeQuery();
			if(rs == null) {
				return null;
			}
			else {
				return rs;
			}
		}catch(SQLException e) {
			System.out.println("SQLE in isLobby: " + e.getMessage());
			return null;
			
		}
	}
	
	// return null if that id does not exsists ..!
	public static List<Integer> getUserFromLobby(int id){
		List<Integer> temp = new ArrayList<>();
		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement("SELECT * FROM LobbyUsers WHERE Lobbies.lobbyId = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			
			while(rs.next()) {
				temp.add(rs.getInt(("userId")));
			}
			//return temp;
			
		}catch(SQLException e) {
			System.out.println("SQLE in gettting users from lobby: " + e.getMessage());
			//return null;
		}
		return temp;
	}
	
	public static List<Integer> getintsFromLobby(int lobbyid){
		List<Integer> temp = new ArrayList<>();
		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement("SELECT * FROM Music WHERE lobbyId = ?");
			ps.setInt(1, lobbyid);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				temp.add(rs.getInt("songId"));
			}
		}catch(SQLException e) {
			System.out.println("SQLE in get Ints from Lobby: " + e.getMessage());
		}
		return temp;
	}
	
	public static void addUserToLobby(int lobbyId, int userId) {
		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement("INSERT INTO LobbyUsers(lobbyId,userId)"
					+ "VALUES(?,?);");
			ps.setInt(1, lobbyId);
			ps.setInt(2,userId);
			int response = ps.executeUpdate();
		}catch(SQLException e) {
			System.out.println("SQLE in adding user in lobby: " + e.getMessage());
		}
	}
	
	public static void addSongToLobby(int lobbyId,int songId) {
	 // do not need a id, need a song file??	
	}
	
	public static void removeSongFromLobby(int lobbyId, int songId) {
		try {
			PreparedStatement ps= null;
			ps  = conn.prepareStatement("DELETE FROM Music WHERE lobbyId = ? and songId =? LIMIT 1");
			ps.setInt(1,lobbyId);
			ps.setInt(2, songId);
			int x =ps.executeUpdate();
		}catch(SQLException e) {
			System.out.println("SQLE in ermove Song from lobby: " + e.getMessage());
		}
	}
	
	public static void moveSongFromLobby(int lobbyId, int songId, String Location) {
		
	}
	
	public static void changeLobbyPublicStatuc(int lobbyId,boolean isPublic) {
		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement("UPDATE Lobbies "
					+ "SET isPublic = ? "
					+ "WHERE lobbyId = ?");
			ps.setBoolean(1, isPublic);
			ps.setInt(2, lobbyId);
			int x = ps.executeUpdate();
		}catch(SQLException e) {
			System.out.println("SQLE in changing lobby public status: " + e.getMessage());
		}
	}
	
	public static void setNameForLobby(int lobbyId, String name) {
		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement("UPDATE Lobbies SET lobbyName=? WHERE lobbyId = ?");
			ps.setString(1, name);
			ps.setInt(2, lobbyId);
			int x = ps.executeUpdate();
		}catch(SQLException e){
			System.out.println("SQLE in setNaem for lobby: " + e.getMessage());
		}
	}
	
	
	public static void setPasswordForLobby(int lobbyId, String password) {
		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement("UPDATE Lobbies SET pswd=? WHERE lobbyId = ?");
			ps.setString(1, password);
			ps.setInt(2, lobbyId);
			int x = ps.executeUpdate();
		}catch(SQLException e){
			System.out.println("SQLE in setNaem for lobby: " + e.getMessage());
		}
	}
	
	
	
	
	
	
	
	
}
	
	/*{
		try {
			this.fname = fname;
			this.lname = lname;
			
			
			
			this.generalResultSet = ps.executeQuery();
			this.generalResultSet.next();
			
			//File f = new File("pic.jpg");
						
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
	
	public static  List<String> getFriendImages() {
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
			
				this.friendImages.add(rs.getString("imgLocation"));
			}	
		}catch(SQLException e) {
			
		}
	}
	
	
	
	
	
}
*/