import java.util.List;

public class User {
    private String username;
    private String password;
    private List<User> friendsList;
//    private String googleID;
    private String songLocation;
    private List<Lobby> favoriteLobbies;
    private List<Lobby> hostedLobbies;
    private boolean platinumUser = false;
    private String chatFilesLocation;
    private String avatar;

    User(String username, String password){
        setUsername(username);
        setPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<User> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(List<User> friendsList) {
        this.friendsList = friendsList;
    }

    public String getSongLocation() {
        return songLocation;
    }

    public void setSongLocation(String songLocation) {
        this.songLocation = songLocation;
    }

    public List<Lobby> getFavoriteLobbies() {
        return favoriteLobbies;
    }

    public void setFavoriteLobbies(List<Lobby> favoriteLobbies) {
        this.favoriteLobbies = favoriteLobbies;
    }

    public List<Lobby> getHostedLobbies() {
        return hostedLobbies;
    }

    public void setHostedLobbies(List<Lobby> hostedLobbies) {
        this.hostedLobbies = hostedLobbies;
    }

    public boolean isPlatinumUser() {
        return platinumUser;
    }

    public void setPlatinumUser(boolean platinumUser) {
        this.platinumUser = platinumUser;
    }

    public String getChatFilesLocation() {
        return chatFilesLocation;
    }

    public void setChatFilesLocation(String chatFilesLocation) {
        this.chatFilesLocation = chatFilesLocation;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void loginUser(String username, String password){
        if(username.equals(getUsername()) && password.equals(getPassword())){
            System.out.print("User " + getUsername() + " is logged in.");
        }
    }

}
