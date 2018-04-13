import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/backend_getUserInfo")
public class backend_getUserInfo extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("----------Getting User Info");
        HttpSession session = request.getSession();
        int testID = Integer.parseInt(request.getParameter("userID"));
//        int loggedInUserID = Integer.parseInt(session.getAttribute("userID").toString());

        //User curLoggedInUser = new User(loggedInUserID);
        User tempUser = new User("email", "username", "password");
        ObjectMapper outputMapper = new ObjectMapper();
        String curUserJsonString = outputMapper.writeValueAsString(tempUser);
        System.out.println(curUserJsonString);

        response.getWriter().write(curUserJsonString);
    }
}
