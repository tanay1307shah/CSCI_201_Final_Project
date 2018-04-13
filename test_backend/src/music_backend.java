import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/backend_logIn")
public class music_backend extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("------LOG IN SERVLET");

        System.out.println("User email is: " + request.getParameter("userEmail"));
        System.out.println("User password is: " + request.getParameter("password"));

        //SOL validation for user info. Danial and Tanay figure this shit out.
        int loggedInUserID = 1;

        if(loggedInUserID != -1) {
            System.out.println("test1");
            HttpSession session = request.getSession();
//            session.setAttribute("loggedInUserID", loggedInUserID);
            System.out.println(session.getAttribute("loggedInUserID"));

            response.getWriter().write("userID: "+loggedInUserID);
        }else{
            response.getWriter().write("WRONG");
        }
    }
}
