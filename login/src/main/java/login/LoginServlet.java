package login;

import jakarta.servlet.*;

import java.io.IOException;
import java.io.PrintWriter;

public class LoginServlet implements Servlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("I am inside init method");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //validate the username and password using database
        if (ValidateUser.validateUser(username, password)) {
            pw.println("<h2>You are authorized!!</h2>");
        } else {
            pw.println("<h2 style=\"color:Tomato;\">You are not authorized</h2>");
        }

        System.out.println("Done!!");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
        System.out.println("I am inside destroy method");
    }
}
