import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.PreparedStatement;
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
 * NOTE: Songs cannot be reordred in the queue as of now
 */
public class Lobby {
    private String name;
    private String password;
    private int host;
    private List<Integer> peopleInLobby;

    public List<String> getPeopleInLobbyString() {
        return peopleInLobbyString;
    }

    public void setPeopleInLobbyString(List<String> peopleInLobbyString) {
        this.peopleInLobbyString = peopleInLobbyString;
    }

    private List<String> peopleInLobbyString = new ArrayList<>();
    private boolean isPublic;
    private List<Integer> songList; //NOT ORDERED, just all the songs in the lobby
    private int lobbyID;
    private int songTime;
    private String chatFilesLocation;

    /** Generates a lobby.
     * Make sure to first check if the lobby name is in use using
     * Database.isLobby(name)
     */
    Lobby(String name, String password, int host, boolean isPublic) {
        this.name = name;
        this.host = host;
        this.isPublic = isPublic;
        songList = new ArrayList<Integer>();
        if (isPublic == false) {
            this.password = password;
        }
        this.lobbyID = Database.createLobby(host, name, password, isPublic);
    }

    /**
     * Constructs a new lobby from the data in the Database
     * Does not change the database
     * Assume the lobby fields are fully populated after this constructor
     */
    Lobby(int lobbyID) {
        this.lobbyID = lobbyID;
        ResultSet rs = Database.getLobby(lobbyID);
        try {
            name = rs.getString("lobbyName");
            password = rs.getString("pswd");
            host = rs.getInt("hostId");
            peopleInLobby = Database.getUsersFromLobby(lobbyID);
            isPublic = rs.getBoolean("isPublic");
            songList = Database.getSongsFromLobby(lobbyID);
//            songTime = rs.getInt("currentTime");
            chatFilesLocation = Database.getChatLocation(lobbyID);
            for(int i : peopleInLobby){
                PreparedStatement ps = null;
                ps = Database.conn.prepareStatement("SELECT username FROM USERS WHERE userId =?");
                ps.setInt(1,i);
                rs = ps.executeQuery();
                rs.next();
                peopleInLobbyString.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getLobbyID() {
        return lobbyID;
    }

    public void setLobbyID(int lobbyID) {
        this.lobbyID = lobbyID;
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
    //Adds a user to the lobby
    //note that the privelege of the user is not checked here
    public void addUser(int userID) {
        peopleInLobby.add(userID);
        Database.addUserToLobby(lobbyID, userID);
    }

    public void addSong(int songID) {
        songList.add(songID);
        Database.addSongToLobby(lobbyID, songID);
    }

    public List<Integer> getints() {
        return songList;
    }

    public void removeSong(int songID) {
        songList.remove(songID);
        Database.removeSongFromLobby(lobbyID, songID);
    }

    public int getHost() {
        return host;
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
