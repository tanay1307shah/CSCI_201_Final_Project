/**
 * A class that represents a single Lobby
 * To create a lobby, first call Database.isLobby(lobbyName) to make sure it
 * isn't taken, then create the lobby here.
 * Only use this class to edit the lobby, any calls to this class will
 * automatically update the database using calls to Database.
 */
public class Lobby {
    private String name;
    private String password;
    private int host;
    private ArrayList<int> peopleInLobby;
    private boolean isPublic;
    private ArrayList<int> songList; //the first item in songlist is currently playing
    private int lobbyID;
    private int songTime;

    /** Generates a lobby.
     * Make sure to first check if the lobby name is in use using
     * Database.isLobby(name)
     */
    Lobby(String name, String password, User host, boolean isPublic) {
        this.name = name;
        this.host = host;
        this.isPublic = isPublic;
        songQueue = new List<int>();
        if (isPublic == false) {
            this.password = password;
        }
        this.lobbyID = Database.createLobby(host.getID(), password, isPublic, true);
    }

    /**
     * Constructs a new lobby from the data in the Database
     * Does not change the database
     * Assume the lobby fields are fully populated after this constructor
     */
    Lobby(int lobbyID) {
        this.lobbyID = lobbyID;
        ResultSet rs = Database.getLobby(lobbyID);
        name = rs.getString("name");
        password = rs.getString("pwd");
        host = rs.getString("hostId");
        peopleInLobby = Database.getUsersFromLobby(lobbyID);
        isPublic = rs.getString("isPublic");
        songList = Database.getintsFromLobby(LobbyID);
        songTime = rs.getInt("currentTime");
    }


    //Adds a user to the lobby
    //note that the privelege of the user is not checked here
    public void addUser(User usr) {
        peopleInLobby.addUser(usr);
        Database.addUserToLobby(lobbyID, usr.getID());
    }

    public void addint(int song) {
        songList.add(song);
        Database.addintToLobby(lobbyID, song.getID());
    }

    public Queue<int> getints() {
        return songList;
    }

    public void removeint(long songID) {
        for(int sng : songList)
        {
            if (sng.getID() == id)
                songList.remove(sng);
        }
        Database.removeintFromLobby(lobbyID, songID);
    }

    public void moveint(long id, int location) {
        for(int i = 0; i < songList.size(); i++)
        {
            if(songList.get(i).getID() == id) {
                int tmp = songList.get(i);
                songList.remove(i);
                songList.add(location, tmp);
            }
        }
        Database.moveintFromLobby(lobbyID, songID, location);
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
        Database.setLobbyHostForLobby(lobbyID, host.getID());
    }

    public UserList getPeopleInLobby() {
        return peopleInLobby;
    }

    public void addPeopleInLobby(User usr) {
        this.peopleInLobby.a;
    }

    public boolean isPublicBool() {
        return isPublic;
    }

    public void setPublicBool(boolean isPublic) {
        this.isPublic = isPublic;
        Database.setPublicBoolForLobby(LobbyID, isPublic);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        Database.setNameForLobby(LobbyID, name);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        Database.setPasswordForLobby(lobbyID, password);
    }
}
