import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/handleEvent")
public class handleEvent extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Database sdb = new Database();
        String event = request.getParameter("event");
        if (event.equals("createLobby")) {
            String lobbyName = request.getParameter("lobbyName");
            String lobbyPassword = request.getParameter("lobbyPassword");
            String hostUserame = request.getParameter("hostName");
            int hostUserID = Database.getUserId(hostUserame);
            User lobbyHost = new User(hostUserID);
            Lobby newLobby = new Lobby(lobbyName, lobbyPassword, lobbyHost, false);
            String lobbyJSON = newLobby.toJson();
            response.getWriter().write("success");

        } else if (event.equals("modifyUser")) {
            String modifiedUsername = request.getParameter("username");
            String userPassword = request.getParameter("userPassword");
            String profileImagge = request.getParameter("profileImagge");
            int modifyUserId = Database.getUserId(modifiedUsername);
            Database.setUsernameForUser(modifyUserId, modifiedUsername);
            Database.setPasswordForUser(modifyUserId, userPassword);
            Database.setImgLocationForUser(modifyUserId, profileImagge);
            response.getWriter().write("success");

        } else if (event.equals("getUserInfo")) { //get a user's info when clicking a friend in friends list, need the user's info in write back
            String username = request.getParameter("username");
            int userID = Database.getUserId(username);
            User returnUser = new User(userID);
            String returnUserJSON = returnUser.toJson();
            response.getWriter().write(returnUserJSON);

        } else if (event.equals("getLobbyInfo")) {//get a lobby's info when clicking a friend in friends list, need the lobby's info in write back
            String username = request.getParameter("lobbyName");
            int lobbyID = Database.getLobbyId(username);
            Lobby currentLobby = new Lobby(lobbyID);
            String returnLobbyJSON = currentLobby.toJson();

//            List<Integer> songIds = Database.getSongsFromLobby(lobbyID);
//            for(int temp : songIds){
//                StringBuilder resultBuilder = new StringBuilder();
//                try {
//                    ResultSet songData = Database.getSongData(temp);
//                    songData.next();
//                    String result = songData.getString("songName");
//                    resultBuilder.append(result).append(",");
//                }catch (SQLException ex){
//                    ex.printStackTrace();
//                }
//                String songOutputs = resultBuilder.toString();

//                StringBuilder outputBuilder = new StringBuilder();
//                outputBuilder.append(returnLobbyJSON).append("||").append(songOutputs);
            response.getWriter().write(returnLobbyJSON);

        }
//        $.get('http://192.168.137.125:8080/handleEvent?event=getChat&lobbyName=somename',function(data){}
        else if (event.equals("getChat")) {
            String lobbyName = request.getParameter("lobbyName");
            int lobbyId = Database.getLobbyId(lobbyName);
            String fileLocation = Database.getChatLocation(lobbyId);
            ServletContext context = getServletContext();
            InputStream is = context.getResourceAsStream(fileLocation);
            StringBuilder chatString = new StringBuilder();
            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isr);
                String text;
                while ((text = reader.readLine()) != null) {
                    chatString.append(text).append('\n');
                }
            }
            String output = chatString.toString();
            response.getWriter().write(output);

        } else if (event.equals("search")) {
            List<String> lobbyList = Database.getAllLobbies();
            List<String> userList = Database.getAllUsers();
            String searchType = request.getParameter("searchType");
            String searchTerm = request.getParameter("searchStr");
            List<String> results = new ArrayList<>();
            if (searchType.equals("user")) {
                for (String temp : userList) {
                    if (temp.startsWith(searchTerm)) {
                        results.add(temp);
                    }
                }
            } else if (searchType.equals("lobby")) {
                for (String temp : userList) {
                    if (temp.startsWith(searchTerm)) {
                        results.add(temp);
                    }
                }
            }
            StringBuilder resonceString = new StringBuilder();
            for (String result : results) {
                resonceString.append(result).append(",");
            }
            String output = resonceString.toString();
            response.getWriter().write(output);
        }


    }
}
