public class Lobby {
    private String name;
    private String password;
    private User host;
    private UserList peopleInLobby;
    boolean publicBool;


    Lobby(String name, String password, User host, boolean publicBool) {
        this.name = name;
        this.host = host;
        this.publicBool = publicBool;
        if (publicBool == false) {
            this.password = password;
        }
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public UserList getPeopleInLobby() {
        return peopleInLobby;
    }

    public void setPeopleInLobby(UserList peopleInLobby) {
        this.peopleInLobby = peopleInLobby;
    }

    public boolean isPublicBool() {
        return publicBool;
    }

    public void setPublicBool(boolean publicBool) {
        this.publicBool = publicBool;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
