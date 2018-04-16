import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;

public class Database {
    static Connection conn;

    public Database() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MUSICRT?user=root&password=root");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static int createUser(String username, String password, String imgLocation, String email) {
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("INSER INTO USERS(username,pwd,imgLocation,email) VALUES (?,?,?,?)");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, imgLocation);
            ps.setString(4, email);
            int x = ps.executeUpdate();
            return x;
        } catch (SQLException e) {
            System.out.println("SQLE in create User: " + e.getMessage());
            return 0;
        }

    }

    //Return -1 if login is wrong
    public static int login(String email, String password) {
        try {
            PreparedStatement ps = null;
            ResultSet rs = null;
            String q = "SELECT * FROM USERS WHERE email=? AND pwd=?";
            ps = conn.prepareStatement(q);
            System.out.println(q);
            System.out.println(email + " " + password);
            ps.setString(1, email);
            ps.setString(2, password);
            rs = ps.executeQuery();
            rs.next();
            if (rs == null) {
                return -1;
            } else {
                return rs.getInt("userId");
            }
        } catch (SQLException e) {
            System.out.println("SQLE in login: " + e.getMessage());
            return -1;
        }
    }

    public static ResultSet getUserData(int id) {
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("SELECT * FROM USERS WHERE userId=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            System.out.println("SQLE in getting user Data: " + e.getMessage());
            return null;
        }
    }

    public static List<Integer> getFriendsFromUser(int id) {
        List<Integer> temp = new ArrayList<>();
        try {
            PreparedStatement ps1 = null;
            ps1 = conn.prepareStatement("SELECT * FROM Friendship WHERE fromUserId=?");
            ps1.setInt(1, id);
            ResultSet rs = ps1.executeQuery();

            while (rs.next()) {
                int tid = rs.getInt("toUserID");
                temp.add(id);
            }

        } catch (SQLException e) {
            System.out.println("SQLE in getting all friends IDs: " + e.getMessage());
        }
        return temp;
    }

    public static List<String> getfriendsName(int id) {
        List<Integer> tempId = getFriendsFromUser(id);
        List<String> tempName = new ArrayList<>();

        for (int i = 0; i < tempId.size(); i++) {
            try {
                PreparedStatement ps = null;
                ps = conn.prepareStatement("SELECT * FROM USERS"
                        + "WHERE Users.userId = ?");
                ps.setInt(1, tempId.get(i));
                ResultSet rs = ps.executeQuery();
                rs.next();
                tempName.add(rs.getString("fname") + "," + rs.getString("lname"));
            } catch (SQLException e) {
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
            if (rs == null) {
                return -1;
            } else {
                return rs.getInt("lobbyId");
            }
        } catch (SQLException e) {
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
            rs.next();
            if (rs == null) {
                return null;
            } else {
                return rs;
            }
        } catch (SQLException e) {
            System.out.println("SQLE in isLobby: " + e.getMessage());
            return null;

        }
    }

    // return null if that id does not exsists ..!
    public static List<Integer> getUsersFromLobby(int id) {
        List<Integer> temp = new ArrayList<>();
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("SELECT * FROM LobbyUsers WHERE Lobbies.lobbyId = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();


            while (rs.next()) {
                temp.add(rs.getInt(("userId")));
            }
            //return temp;

        } catch (SQLException e) {
            System.out.println("SQLE in gettting users from lobby: " + e.getMessage());
            //return null;
        }
        return temp;
    }

    public static void addUserToLobby(int lobbyId, int userId) {
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("INSERT INTO LobbyUsers(lobbyId,userId)"
                    + "VALUES(?,?);");
            ps.setInt(1, lobbyId);
            ps.setInt(2, userId);
            int response = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLE in adding user in lobby: " + e.getMessage());
        }
    }

    public static void addSongToLobby(int lobbyId, int songId) {
        // do not need a id, need a song file??
    }

    public static List<Integer> getSongsFromLobby(int lobbyid) {
        List<Integer> temp = new ArrayList<>();
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("SELECT * FROM Music WHERE lobbyId = ?");
            ps.setInt(1, lobbyid);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                temp.add(rs.getInt("songId"));
            }
        } catch (SQLException e) {
            System.out.println("SQLE in get Ints from Lobby: " + e.getMessage());
        }
        return temp;
    }

    /**
     * ASK AND MIGHT HAVE TO CHANGE
     *
     * @param lobbyId
     * @param songId
     */
    public static void removeSongFromLobby(int lobbyId, int songId) {
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("DELETE FROM Music WHERE lobbyId = ? and songId =? LIMIT 1");
            ps.setInt(1, lobbyId);
            ps.setInt(2, songId);
            int x = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLE in ermove Song from lobby: " + e.getMessage());
        }
    }

    public static void setNameForLobby(int lobbyId, String name) {
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("UPDATE Lobbies SET lobbyName=? WHERE lobbyId = ?");
            ps.setString(1, name);
            ps.setInt(2, lobbyId);
            int x = ps.executeUpdate();
        } catch (SQLException e) {
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
        } catch (SQLException e) {
            System.out.println("SQLE in setNaem for lobby: " + e.getMessage());
        }
    }


    public static List<Integer> getFavoriteLobbiesFromUser(int userId) {
        List<Integer> temp = new ArrayList<>();
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("SELECT * FROM favLobbies WHERE userId = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                temp.add(rs.getInt("lobbyId"));
            }

        } catch (SQLException e) {
            System.out.println("SQLE in getting Fav Lobbies: " + e.getMessage());
        }

        return temp;
    }

    public static List<Integer> getHostedLobbiesFromUser(int userId) {
        List<Integer> temp = new ArrayList<>();
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("SELECT * FROM Lobbies WHERE userId = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                temp.add(rs.getInt("lobbyId"));
            }
        } catch (SQLException e) {
            System.out.println("SQLE in getting hosted lobbies for user " + userId + ": " + e.getMessage());
        }
        return null;
    }


    public static void setUsernameForUser(int userId, String userName) {
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("UPDATE Users SET username = ? WHERE userId =?");
            ps.setString(1, userName);
            ps.setInt(2, userId);
            int x = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLE in setting username: " + e.getMessage());
        }
    }


    public static void setPasswordForUser(int userId, String password) {
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("UPDATE Users SET pwd = ? WHERE userId =?");
            ps.setString(1, password);
            ps.setInt(2, userId);
            int x = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLE in setting password: " + e.getMessage());
        }
    }

    public static void addFriendToUser(int userId, int friendId) {
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("INSERT INTO Friendship(fromUserId,toUserId)");
            ps.setInt(1, userId);
            ps.setInt(2, friendId);
            int x = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLE in add friend: " + e.getMessage());
        }
    }

    public static void setPlatinumForUser(int userId, boolean plattinumStatus) {
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("UPDATE Users SET plattinumUser = ? WHERE userId =?");
            ps.setBoolean(1, plattinumStatus);
            ps.setInt(2, userId);
        } catch (SQLException e) {
            System.out.println("SQLE in settting user plattinumstatus: " + e.getMessage());
        }
    }

    //ASK , and need the to id as well!!
    public static void setChatFilesLocationForLobby(int lobbyId, String location) {
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("UPDATE Chats SET chatLoc=? WHERE lobbyId = ?");
            ps.setString(1, location);
            ps.setInt(2, lobbyId);
            int x = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLE in setting chat location: " + e.getMessage());
        }
    }

    public static void setImgLocationForUser(int userId, String location) {
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("UPDATE User SET imgLocation =? WHERE userId =?");
            ps.setString(1, location);
            ps.setInt(2, userId);
            int x = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLE in setting user Image location: " + e.getMessage());
        }
    }


    public static void setIsPublicForLobby(int lobbyID, boolean isPublic) {
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("UPDATE Lobbies SET isPublic =? WHERE lobbyId =?");
            ps.setBoolean(1, isPublic);
            ps.setInt(2, lobbyID);
            int x = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLE in set public status of lobby: " + e.getMessage());
        }
    }


    public static void addLobbyToFavoritesForUser(int userId, int lobbyId) {
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("INSERT INTO favLobbies(lobbyId,userId) VALUES (?,?)");
            ps.setInt(1, lobbyId);
            ps.setInt(2, userId);
            int x = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLE in adding lobby to favs for user: " + e.getMessage());
        }
    }

    public static int createLobby(int hostId, String lobbyName, String password, boolean isPublic) {
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("INSERT INTO Lobbies(hostId,lobbyName,pswd) VALUES(?,?,?)");
            ps.setInt(1, hostId);
            ps.setString(2, lobbyName);
            ps.setString(3, password);
            int x = ps.executeUpdate();
            setIsPublicForLobby(x, isPublic);
            return x;
        } catch (SQLException e) {
            System.out.println("SQLE in add a new lobby: " + e.getMessage());
        }
        return -1;
    }

    public static int createSong(int lobbyId, String location) {
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("INSERT INTO Music(lobbyId,slocation) VALUES(?,?)");
            ps.setInt(1, lobbyId);
            ps.setString(2, location);
            int x = ps.executeUpdate();
            return x;
        } catch (SQLException e) {
            System.out.println("SQLE in create song: " + e.getMessage());
        }
        return -1;
    }

    public static ResultSet getSongData(int songId) {
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("SELECT * FROM Music WHERE songId = ?");
            ps.setInt(1, songId);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            System.out.println("SQLE in getSongData: " + e.getMessage());
        }
        return null;
    }

    public static String getChatLocation(int lobbyId){
        String loc = "";
        try{
            PreparedStatement ps = null;
            ps = conn.prepareStatement("SELECT * FROM Chats WHERE lobbyId =?");
            ps.setInt(1,lobbyId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            loc = rs.getString("chatLoc");
        }catch(SQLException e){
            System.out.println("SQLE in get Chat loc: " + e.getMessage());
        }
        return loc;
    }

    public static int getUserId(String username) {
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("SELECT userId FROM MUSICRT.USERS WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("userId");
        } catch (SQLException e) {
            System.out.println("SQLE in getUserId: " + e.getMessage());
        }
        return -1;
    }

    public static int getLobbyId(String lobbyname) {
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("SELECT lobbyId FROM Lobbies WHERE lobbyName =?");
            ps.setString(1, lobbyname);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("lobbyId");
        } catch (SQLException e) {
            System.out.println("SQLE in getLobbyId: " + e.getMessage());
        }
        return -1;
    }

    public static List<String> getAllUsers() {
        List<String> temp = new ArrayList<>();
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("SELECT username FROM USERS;");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                temp.add(rs.getString("username"));
            }

        } catch (SQLException e) {
            System.out.println("SQLE in getAllUsers: " + e.getMessage());
        }
        return temp;
    }

    public static List<String> getAllLobbies() {
        List<String> temp = new ArrayList<>();
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("SELECT lobbyName FROM Lobbies;");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                temp.add(rs.getString("lobbyName"));
            }

        } catch (SQLException e) {
            System.out.println("SQLE in getAllLobbies: " + e.getMessage());
        }
        return temp;
    }


}