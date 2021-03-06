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

    private static Vector<Session> sessions = new Vector<Session>();

    @OnOpen
    public void open(Session session) {
        System.out.println("Connection made!");
        sessions.add(session);
        System.out.println("Session Size: " + sessions.size());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        String[] parts = message.split("~"); //split message at every '~'

        String event = parts[0]; //first part is event
        String action = parts[1]; //second part is the action
        String lobbyName = parts[2]; //lobby to send info to

        int currentLobbyId = Database.getLobbyId(lobbyName);
        Lobby currentLobby = new Lobby(currentLobbyId);
        List<Integer> useresInCurLobby = currentLobby.getPeopleInLobby();
        StringBuilder userListBuilder = new StringBuilder();
        for (int i = 0; i < useresInCurLobby.size(); i++) {
            boolean isSame = false;
            for(int j = 0; j < i; j++){
                if(useresInCurLobby.get(i).equals(useresInCurLobby.get(j))){
                    isSame = true;
                }
            }
            if(!isSame) {
                userListBuilder.append(Integer.toString(useresInCurLobby.get(i))).append(",");
            }
        }
        userListBuilder.append("99,");
        String userList = userListBuilder.toString();

        if (event.equals("MusicControl")) {
            double currentTime = Double.parseDouble(parts[3]);
            if (action.equals("PlayMusic")) {  //SENDS OUT "PlayMusic~peopleInLobby~lobbyId~currentTime"
                String output = "PlayMusic~" + userList + "~" + currentLobbyId + "~" + currentTime;
                for (Session users : sessions) {
                    try {
                        users.getBasicRemote().sendText(output);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            } else if (action.equals("StopMusic")) {
                String output = "StopMusic~" + userList + "~" + currentLobbyId + "~" + currentTime;
                for (Session users : sessions) {
                    try {
                        users.getBasicRemote().sendText(output);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } else if (action.equals("PeviousButton")) {
                String output = "PeviousButton~" + userList + "~" + currentLobbyId + "~" + currentTime;
                for (Session users : sessions) {
                    try {
                        users.getBasicRemote().sendText(output);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } else if (action.equals("UpdateCurrentTime")) {
                String output = "UpdateCurrentTime~" + userList + "~" + currentLobbyId + "~" + currentTime;
                for (Session users : sessions) {
                    try {
                        users.getBasicRemote().sendText(output);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } else if (action.equals("NextButton")) {
                String output = "NextButton~" + userList + "~" + currentLobbyId + "~" + currentTime;
                for (Session users : sessions) {
                    try {
                        users.getBasicRemote().sendText(output);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } else if (event.equals("Message")) {//event
            String user = parts[3];
            String messageToSend = parts[4];
            String totalMsg = user + ": " + messageToSend;
            String output = "SendMessage~" + userList + "~" + currentLobbyId + "~" + totalMsg;
            for (Session users : sessions) {
                if (users != session) {
                    try {
                        users.getBasicRemote().sendText(output);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
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
