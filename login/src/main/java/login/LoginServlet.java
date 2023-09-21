package login;

import jakarta.servlet.*;

import java.io.IOException;
import java.io.PrintWriter;

public class LoginServlet implements Servlet {

    @Override
    public void init(ServletConfig servletConfig) {
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
        User user = ValidateUser.validateUser(username, password);
        if (user !=null) {
            if(user.getUsername().equalsIgnoreCase("admin"))
            {
                request.setAttribute("username",username.toUpperCase());
            }
            else{
                request.setAttribute("username",username);
            }
            if(user.getRole().equalsIgnoreCase("HR")){
                request.setAttribute("attendance","HR Attendance");
                request.setAttribute("leaves","Employee Leaves");
                request.setAttribute("myprofile","Employee Profile");
            }
            else if(user.getRole().equalsIgnoreCase("EMP")){
                request.setAttribute("attendance","My Attendance");
                request.setAttribute("leaves","My Leaves");
                request.setAttribute("myprofile","My Profile");
            }
            else {
                request.setAttribute("attendance","Attendance");
                request.setAttribute("leaves","Leaves");
                request.setAttribute("myprofile","My Profile");
            }

            RequestDispatcher rd = request.getRequestDispatcher("/dash.jsp");
            rd.include(request,response);
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
