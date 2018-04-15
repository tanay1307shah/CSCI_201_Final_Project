import java.sql.ResultSet;
import java.sql.SQLException;
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
    private String imgLocation;
    private List<Integer> favoriteLobbies;
    private List<Integer> hostedLobbies;
    private boolean platinumUser = false;
    private String chatFilesLocation;
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

    /**
     * Constructs a new user from the data in the Database
     * Does not change the database at all
     * Assume the user fields are fully populated after this constructor
     */
    User(int userID) {
        this.userID = userID;
        ResultSet rs = Database.getUser(userID);
        try {
            rs.next();
            username = rs.getString("Username");
            password = rs.getString("pwd");
            email = rs.getString("email");
            //songLocation = rs.getString("slocation");
            imgLocation = rs.getString("imgLocation");
            chatFilesLocation = rs.getString("chatLoc");
            friendsList = Database.getFriendsFromUser(userID);
            favoriteLobbies = Database.getFavoriteLobbiesFromUser(userID);
            hostedLobbies = Database.getHostedLobbiesFromUser(userID);
            platinumUser = rs.getBoolean("platinumUser");
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

    public void setFriendsList(List<Integer> friendsList) {
        this.friendsList = friendsList;
        Database.setFriendsListForUser(userID, friendsList);
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

    public void setFavoriteLobbies(List<Integer> favoriteLobbies) {
        this.favoriteLobbies = favoriteLobbies;
        Database.setFavoriteLobbiesForUser(userID, favoriteLobbies);
    }

    public void addLobbyToFavorites(int lobbyID) {
        this.favoriteLobbies.add(lobbyID);
        Database.addLobbyToFavoritesForUser(userID, lobbyID);
    }

    public List<Integer> getHostedLobbies() {
        return hostedLobbies;
    }

    public void setHostedLobbies(List<Integer> hostedLobbies) {
        this.hostedLobbies = hostedLobbies;
        Database.setHostedLobbiesForUser(userID, hostedLobbies);
    }

    public void addLobbyToHosted(int lobbyID) {
        this.hostedLobbies.add(lobbyID);
        Database.addLobbyToHostedForUser(userID, lobbyID);
    }

    public boolean isPlatinumUser() {
        return platinumUser;
    }

    public void setPlatinumUser(boolean platinumUser) {
        this.platinumUser = platinumUser;
        Database.setPlatinumForUser(userID, platinumUser);
    }

    public String getChatFilesLocation() {
        return chatFilesLocation;
    }

    public void setChatFilesLocation(String chatFilesLocation) {
        this.chatFilesLocation = chatFilesLocation;
        Database.setChatFilesLocationForUser(userID, chatFilesLocation);
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

}
