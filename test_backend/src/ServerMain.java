import javafx.util.Pair;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/ws")
public class ServerMain {

    private Vector<Session> sessions = new Vector<>();

    @OnOpen
    public void open(Session session) {
        System.out.println("Connection made!");
        //sessions.put(Integer.parseInt(ev), session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        //String string = "004-034556";
        String[] parts = message.split("|~|");
        String event = parts[0]; // 004
        if (event.equals("MusicControl")) {
            String action = parts[1];
            String lobbyName = parts[2];
            int currentLobbyId = Database.getLobbyId(lobbyName);
            Lobby currentLobby = new Lobby(currentLobbyId);
            List<Integer> useresInCurLobby = currentLobby.getPeopleInLobby();
            StringBuilder userListBuilder = new StringBuilder();
            for (int temp : useresInCurLobby) {
                userListBuilder.append(Integer.toString(temp)).append(",");
            }
            String userList = userListBuilder.toString();

            if (action.equals("PlayMusic")) {  //SENDS OUT "PlayMusic|~|1,2,3"
                String output = "PlayMusic|~|" + userList;
                for (Session users : sessions) {
                    try {
                        users.getBasicRemote().sendText(output);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            } else if (action.equals("StopMusic")) {
                String output = "StopMusic|~|" + userList;
                for (Session users : sessions) {
                    try {
                        users.getBasicRemote().sendText(output);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
//        try {
//            for(Pair<Integer, Session> userSocet : sessions) {
//                userSocet.getValue().getBasicRemote().sendText(message);
//            }
//        } catch (IOException ioe) {
//            System.out.println("ioe: " + ioe.getMessage());
//            close(session);
//        }
    }

    @OnClose
    public void close(Session session) {
        System.out.println("Disconnecting!");
        sessions.remove(session);
    }

    @OnError
    public void error(Throwable error) {
        System.out.println("Error!");
    }
}
