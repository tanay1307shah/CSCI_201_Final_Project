import javafx.util.Pair;

import java.io.IOException;
import java.lang.reflect.Array;
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

    private Map<Integer, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void open(Session session) {
        System.out.println("Connection made!");
        //sessions.put(Integer.parseInt(ev), session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        String inputMessage = message;
        String string = "004-034556";
        String[] parts = string.split("|~|");
        String event = parts[0]; // 004
        if(event.equals("MusicControl")){

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
//        sessionVector.remove(session);
    }

    @OnError
    public void error(Throwable error) {
        System.out.println("Error!");
    }
}
