import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "handleEvent")
public class handleEvent extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String event = request.getParameter("event");
        if(event.equals("createLobby")){
            String lobbyName = request.getParameter("lobbyName");
            String lobbyPassword = request.getParameter("lobbyPassword");
            String hostUserame = request.getParameter("hostName");
            int hostUserID = Database.getUserId(hostUserame);
            User lobbyHost = new User(hostUserID);
            Lobby newLobby = new Lobby(lobbyName, lobbyPassword, lobbyHost, false);
            String lobbyJSON = newLobby.toJson();
            response.getWriter().write("success");

        }else if(event.equals("modifyUser")){
            String modifiedUsername = request.getParameter("username");
            String userPassword = request.getParameter("userPassword");
            String profileImagge = request.getParameter("profileImagge");
            int modifyUserId = Database.getUserId(modifiedUsername);
            Database.setUsernameForUser(modifyUserId, modifiedUsername);
            Database.setPasswordForUser(modifyUserId, userPassword);
            Database.setImgLocationForUser(modifyUserId, profileImagge);
            response.getWriter().write("success");

        }else if(event.equals("getUserInfo")){ //get a user's info when clicking a friend in friends list, need the user's info in write back
            String username = request.getParameter("username");
            int userID = Database.getUserId(username);
            User returnUser = new User(userID);
            String returnUserJSON = returnUser.toJson();
            response.getWriter().write(returnUserJSON);

        }else if(event.equals("getLobbyInfo")){//get a lobby's info when clicking a friend in friends list, need the lobby's info in write back
            String username = request.getParameter("username");
            int lobbyID = Database.getLobbyId(username);
            Lobby currentLobby = new Lobby(lobbyID);
            String returnLobbyJSON = currentLobby.toJson();
            response.getWriter().write(returnLobbyJSON);

        }else if(event.equals("search")){
            List<String> lobbyList = Database.getAllLobbies();
            List<String> userList = Database.getAllUsers();
            String searchType = request.getParameter("searchType");
            String searchTerm = request.getParameter("searchStr");
            if(searchType.equals("user")){

            }else if (searchType.equals("lobby")){

            }

        }

    }
}
