import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Contains all the data, setters, and getters for User
 * Also manages connections to the database
 */
public class User {
    private String username;
    private String password;
    private String email;
    private List<Integer> friendsList;
    private List<String> friendsListStrings;
    private String imgLocation;
    private List<Integer> favoriteLobbies;
    private List<String> favoriteLobbiesString = new ArrayList<>();
    private List<Integer> hostedLobbies;
    private List<String> hostedLobbiesString = new ArrayList<>();
    private boolean platinumUser = false;
    // private String songLocation;
    private int userID;

    /**
     * Constructs a new User. Be sure to check that the username is unique
     * before creation 
     */
    User(String username, String password, String imgLocation, String email){
        this.username = username;
        this.password = password;
        this.imgLocation = imgLocation;
        this.email = email;
        userID = Database.createUser(username, password, imgLocation, email);
    }

    public List<String> getFavoriteLobbiesString() {
        return favoriteLobbiesString;
    }

    public void setFavoriteLobbiesString(List<String> favoriteLobbiesString) {
        this.favoriteLobbiesString = favoriteLobbiesString;
    }

    /**
     * Constructs a new user from the data in the Database
     * Does not change the database at all
     * Assume the user fields are fully populated after this constructor
     */
    User(int userID) {
        this.userID = userID;
        ResultSet rs = Database.getUserData(userID);
        try {
            rs.next();
            username = rs.getString("Username");
            password = rs.getString("pwd");
            email = rs.getString("email");
            //songLocation = rs.getString("slocation");
            imgLocation = rs.getString("imgLocation");
            friendsList = Database.getFriendsFromUser(userID);
            if(friendsList == null){
                friendsList = new ArrayList<>();
            }
            favoriteLobbies = Database.getFavoriteLobbiesFromUser(userID);
            if(favoriteLobbies == null){
                favoriteLobbies = new ArrayList<>();
            }
            hostedLobbies = Database.getHostedLobbiesFromUser(userID);
            if(hostedLobbies == null){
                hostedLobbies = new ArrayList<>();
            }
            platinumUser = rs.getBoolean("plattinumUser");
            PreparedStatement ps = null;
            friendsListStrings = new ArrayList<>();
            for(int i : friendsList){
                ps = Database.conn.prepareStatement("SELECT username FROM MUSICRT.USERS WHERE userId = ?");
                ps.setString(1, Integer.toString(i));
                rs = ps.executeQuery();
                rs.next();
                String userNameNew = rs.getString("username");
                friendsListStrings.add(userNameNew);
            }
            for(int i : favoriteLobbies){
                ps = Database.conn.prepareStatement("SELECT lobbyName FROM MUSICRT.Lobbies WHERE lobbyId = ?");
                ps.setString(1, Integer.toString(i));
                rs = ps.executeQuery();
                rs.next();
                String lobbyNameNew = rs.getString("lobbyName");
                favoriteLobbiesString.add(lobbyNameNew);
            }
        } catch (SQLException e) { //TODO: Actually do something here
            e.printStackTrace();
        }
    }

    /**
     * Returns a Json string using Jackson
     * Returns empty string if Jackson fails (somehow)
     */
    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public int getID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        Database.setUsernameForUser(userID, username);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        Database.setPasswordForUser(userID, password);
    }

    public List<Integer> getFriendsList() {
        return friendsList;
    }

    public void addFriendToUser(int otherUserID) {
        this.friendsList.add(otherUserID);
        Database.addFriendToUser(userID, otherUserID);
    }

    //public String getSongLocation() {
    //    return songLocation;
    //}

    //public void setSongLocation(String songLocation) {
    //    this.songLocation = songLocation;
    //    Database.setSongLocationForUser(userID, songLocation);
    //}

    public List<Integer> getFavoriteLobbies() {
        return favoriteLobbies;
    }

    public void addLobbyToFavorites(int lobbyID) {
        this.favoriteLobbies.add(lobbyID);
        Database.addLobbyToFavoritesForUser(userID, lobbyID);
    }

    public List<Integer> getHostedLobbies() {
        hostedLobbies = Database.getHostedLobbiesFromUser(userID);
        return hostedLobbies;
    }

    public boolean isPlatinumUser() {
        return platinumUser;
    }

    public void setPlatinumUser(boolean platinumUser) {
        this.platinumUser = platinumUser;
        Database.setPlatinumForUser(userID, platinumUser);
    }

    public String getImgLocation() {
        return imgLocation;
    }

    public void setImgLocation(String imgLocation) {
        this.imgLocation = imgLocation;
        Database.setImgLocationForUser(userID, imgLocation);
    }

    public void loginUser(String username, String password){
        if(username.equals(getUsername()) && password.equals(getPassword())){
            System.out.print("User " + getUsername() + " is logged in.");
        }
    }

    public List<String> getFriendsListStrings() {
        return friendsListStrings;
    }

    public void setFriendsListStrings(List<String> friendsListStrings) {
        this.friendsListStrings = friendsListStrings;
    }
}
