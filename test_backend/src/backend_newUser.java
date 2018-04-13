import com.fasterxml.jackson.databind.ObjectMapper;


import javax.jms.ServerSession;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/backend_newUser")
public class backend_newUser extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("------SIGN UP SERVLET");
        String userEmail = request.getParameter("userEmail");
        String password = request.getParameter("password");
        String username = request.getParameter("userName");

        //right here would be sent to the SQL server. Danial or Tanay figure this shit out.
//        Database newDB = new Database();
//        int loggedInUserID = newDB.login(username, password);
        int loggedInUserID = 1;

        if(loggedInUserID != -1) {
            System.out.println("test1");
            HttpSession session = request.getSession();
            session.setAttribute("userID", loggedInUserID);
            System.out.println(session.getAttribute("userID"));

            response.getWriter().write("userID: "+loggedInUserID);
        }else{
            response.getWriter().write("WRONG");
        }
    }
}
