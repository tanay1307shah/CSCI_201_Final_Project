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
	
	
	/**
	 * ASK AND MIGHT HAVE TO CHANGE
	 * @param lobbyId
	 * @param songId
	 */
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
	
	
	public static List<Integer> getFavLobbies(int userId){
		List<Integer> temp = new ArrayList<>();
		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement("SELECT * FROM favLobbies WHERE userId = ?");
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				temp.add(rs.getInt("lobbyId"));
			}
			
		}catch(SQLException e) {
			System.out.println("SQLE in getting Fav Lobbies: " + e.getMessage());
		}
		
		return temp;
	}
	
	public static List<Integer> getHostedLobbies(int userId){
		List<Integer> temp = new ArrayList<>();
		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement("SELECT * FROM Lobbies WHERE userId = ?");
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				temp.add(rs.getInt("lobbyId"));
			}
		}catch(SQLException e) {
			System.out.println("SQLE in getting hosted lobbies for user " + userId + ": " + e.getMessage());
		}
		return null;
	}
	
	
	public static void setUserName(int userId,String userName) {
		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement("UPDATE Users SET username = ? WHERE userId =?");
			ps.setString(1, userName);
			ps.setInt(2, userId);
			int x = ps.executeUpdate();
		}catch(SQLException e) {
			System.out.println("SQLE in setting username: " + e.getMessage());
		}
	}
	
	
	public static void setUserPassword(int userId, String password) {
		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement("UPDATE Users SET pwd = ? WHERE userId =?");
			ps.setString(1, password);
			ps.setInt(2, userId);
			int x = ps.executeUpdate();
		}catch(SQLException e) {
			System.out.println("SQLE in setting password: " + e.getMessage());
		}
	}
	
	
	public static void setUserFriendlist(int userId, List<Integer> friendList) {
		
	}
	
	
	public static void setSongLocation(int userId, String songLocation) {
		
	}
	
	/*public static void setFavouoriteLobbies(int userId, int lobbyId) {
		
	}*/
	
	
	public static void hostNewLobby(int userId) {
		
	}
	
	public static void setUserPlattinumStatus(int userId, boolean plattinumStatus) {
		try {
			PreparedStatement ps =null;
			ps = conn.prepareStatement("UPDATE Users SET plattinumUser = ? WHERE userId =?");
			ps.setBoolean(1, plattinumStatus);
			ps.setInt(2, userId);
		}catch(SQLException e) {
			System.out.println("SQLE in settting user plattinumstatus: " +e.getMessage());
		}
	}
	
	//ASK , and need the to id as well!!
	public static void setChatFilesLocation(int userId,String location) {
		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement("UPDATE Chats SET ");
		}catch(SQLException e){
			System.out.println("SQLE in setting chat location: " + e.getMessage());
		}
	}
	
	public static void setImgLocation(int userId, String location) {
		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement("UPDATE User SET imgLocation =? WHERE userId =?");
			ps.setString(1,location);
			ps.setInt(2, userId);
			int x = ps.executeUpdate();
		}catch(SQLException e) {
			System.out.println("SQLE in setting user Image location: " + e.getMessage());
		}
	}
	
}