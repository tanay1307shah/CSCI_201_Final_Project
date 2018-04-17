import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.awt.image.DataBuffer;
import java.io.IOException;

@WebServlet("/backend_getUserInfo")
public class backend_getUserInfo extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        System.out.println("----------Getting User Info");
        HttpSession session = request.getSession();
        int testUserID = Integer.parseInt(session.getAttribute("userID").toString());

        User currentUser = new User(testUserID);
        ObjectMapper mapper = new ObjectMapper();
        String result;
        try {
            result = mapper.writeValueAsString(currentUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            result = "";
        }
        System.out.println(result);
        response.getWriter().write(result);
    }
}
