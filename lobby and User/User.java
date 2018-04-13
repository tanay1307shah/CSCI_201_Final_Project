import java.util.List;

/**
 * Contains all the data, setters, and getters for User
 * Also manages connections to the database
 */
public class User {
    private String username;
    private String password;
    private List<Integer> friendsList;
    private String songLocation;
    private String imgLocation;
    private List<Integer> favoriteLobbies;
    private List<Integer> hostedLobbies;
    private boolean platinumUser = false;
    private String chatFilesLocation;
    private long userID;

    /**
     * Constructs a new User. Be sure to check that the username is unique
     * before creation 
     */
    User(String username, String password){
        setUsername(username);
        setPassword(password);
        userID = Database.createUser(username, password);
    }

    /**
     * Constructs a new user from the data in the Database
     * Does not change the database at all
     * Assume the user fields are fully populated after this constructor
     */
    User(int userID) {
        this.userID = userID;
        ResultSet rs = Database.getUser(userID);
        username = rs.getString("Username");
        password = rs.getString("pwd");
        songLocation = rs.getString("slocation");
        imgLocation = rs.getString("imgLocation");
        chatFilesLocation = rs.getString("chatLoc");
        friendsList = Database.getFriendsFromUser(userID);
        favoriteLobbies = Database.getFavoriteLobbiesFromUser(userID);
        hostedLobbies = Database.getHostedLobbiesFromUser(userID);
        platinumUser = rs.getBoolean("platinumUser");
    }

    /**
     * Returns a Json string using Jackson
     */
    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public long getID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        Database.setUsernameForUser(UserID, username);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        Database.setPasswordForUser(UserID, username);
    }

    public List<User> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(List<Integer> friendsList) {
        this.friendsList = friendsList;
        Database.setFriendsListForUser(UserID, friendsList);
    }

    public String getSongLocation() {
        return songLocation;
    }

    public void setSongLocation(String songLocation) {
        this.songLocation = songLocation;
        Database.setSongLocationForUser(UserID, songLocation);
    }

    public List<Lobby> getFavoriteLobbies() {
        return favoriteLobbies;
    }

    public void setFavoriteLobbies(List<Integer> favoriteLobbies) {
        this.favoriteLobbies = favoriteLobbies;
        Database.setFavoriteLobbiesForUser(UserID, favoriteLobbies);
    }

    public List<Lobby> getHostedLobbies() {
        return hostedLobbies;
    }

    public void setHostedLobbies(List<Lobby> hostedLobbies) {
        this.hostedLobbies = hostedLobbies;
        Database.setHostedLobbiesForUser(UserID, hostedLobbies);
    }

    public boolean isPlatinumUser() {
        return platinumUser;
    }

    public void setPlatinumUser(boolean platinumUser) {
        this.platinumUser = platinumUser;
        Database.setPlatinumForUser(UserID, platinumUser);
    }

    public String getChatFilesLocation() {
        return chatFilesLocation;
    }

    public void setChatFilesLocation(String chatFilesLocation) {
        this.chatFilesLocation = chatFilesLocation;
        Database.setChatFilesLocationForUser(UserID, chatFilesLocation);
    }

    public String getImgLocation() {
        return imgLocation;
    }

    public void setImgLocation(String imgLocation) {
        this.imgLocation = imgLocation;
        Database.setImgLocationForUser(UserID, imgLocation);
    }

    public void loginUser(String username, String password){
        if(username.equals(getUsername()) && password.equals(getPassword())){
            System.out.print("User " + getUsername() + " is logged in.");
        }
    }

}
