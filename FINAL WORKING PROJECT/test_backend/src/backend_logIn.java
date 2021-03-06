import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/backend_logIn")
public class backend_logIn extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        response.setHeader("Access-Control-Allow-Origin", "*");
        System.out.println("------LOG IN SERVLET");

        System.out.println("User email is: " + request.getParameter("userEmail"));
        System.out.println("User password is: " + request.getParameter("password"));

        String username = request.getParameter("userEmail");
        String password = request.getParameter("password");


        Database newDB = new Database();
        int loggedInUserID = Database.login(username, password);

        if(loggedInUserID != -1) {
            HttpSession session = request.getSession();
            session.setAttribute("userID", loggedInUserID);
            response.getWriter().write("OK");
            return;
        }
        response.getWriter().write("WRONG");
    }
}
