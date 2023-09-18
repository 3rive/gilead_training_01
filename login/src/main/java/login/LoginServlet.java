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
        System.out.println("I am inside service method");
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        pw.println("<h2>Hello from Training Servlet</h2>");
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
