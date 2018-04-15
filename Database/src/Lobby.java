import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    private ArrayList<Integer> peopleInLobby;
    private boolean isPublic;
    private ArrayList<Integer> songList; //the first item in songlist is currently playing
    private int lobbyID;
    private int songTime;

    /** Generates a lobby.
     * Make sure to first check if the lobby name is in use using
     * Database.isLobby(name)
     */
    Lobby(String name, String password, User host, boolean isPublic) {
        this.name = name;
        this.host = host.getID();
        this.isPublic = isPublic;
        songList = new ArrayList<Integer>();
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
        rs.next();
        try {
            name = rs.getString("name");
            password = rs.getString("pwd");
            host = rs.getInt("hostId");
            peopleInLobby = Database.getUsersFromLobby(lobbyID);
            isPublic = rs.getBoolean("isPublic");
            songList = Database.getSongsFromLobby(lobbyID);
            songTime = rs.getInt("currentTime");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    //Adds a user to the lobby
    //note that the privelege of the user is not checked here
    public void addUser(User usr) {
        peopleInLobby.addUser(usr);
        Database.addUserToLobby(lobbyID, usr.getID());
    }

    public void addSong(int songID) {
        songList.add(songID);
        Database.addSongToLobby(lobbyID, songID);
    }

    public List<Integer> getints() {
        return songList;
    }

    public void removeSong(int songID) {
        for(int sng : songList)
        {
            if (sng.getID() == id)
                songList.remove(sng);
        }
        Database.removeSongFromLobby(lobbyID, songID);
    }

    public void moveint(int songID, int index) {
        for(int i = 0; i < songList.size(); i++)
        {
            if(songList.get(i) == songID) {
                int tmp = songList.get(i);
                songList.remove(i);
                songList.add(index, tmp);
            }
        }
        Database.moveSongFromLobby(lobbyID, songID, index);
    }

    public int getHost() {
        return host;
    }

    public void setHost(int host) {
        this.host = host;
        Database.setLobbyHostForLobby(lobbyID, host);
    }

    public List<Integer> getPeopleInLobby() {
        return peopleInLobby;
    }

    public void addPeopleToLobby(int usr) {
        this.peopleInLobby.add(usr);
        Database.addUserToLobby(lobbyID, usr);
    }

    public boolean isPublicBool() {
        return isPublic;
    }

    public void setPublicBool(boolean isPublic) {
        this.isPublic = isPublic;
        Database.setIsPublicForLobby(lobbyID, isPublic);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        Database.setNameForLobby(lobbyID, name);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        Database.setPasswordForLobby(lobbyID, password);
    }
}
