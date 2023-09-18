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
        System.out.println("Request Object - Username" +request.getParameter("username"));
        System.out.println("Request Object - password" +request.getParameter("password"));
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        if (request.getParameter("username").equalsIgnoreCase("admin")
                && request.getParameter("password").equalsIgnoreCase("admin")) {
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
