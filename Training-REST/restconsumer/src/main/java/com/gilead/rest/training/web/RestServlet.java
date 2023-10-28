package com.gilead.rest.training.web;

import com.gilead.rest.training.dto.PersonDTO;
import com.gilead.rest.training.service.PersonService;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
@WebServlet( urlPatterns = "/api")
public class RestServlet extends HttpServlet {
    /**
     * Called by the server (via the <code>service</code> method) to allow a servlet to handle a GET request.
     *
     * <p>
     * Overriding this method to support a GET request also automatically supports an HTTP HEAD request. A HEAD request is a
     * GET request that returns no body in the response, only the request header fields.
     *
     * <p>
     * When overriding this method, read the request data, write the response headers, get the response's writer or output
     * stream object, and finally, write the response data. It's best to include content type and encoding. When using a
     * <code>PrintWriter</code> object to return the response, set the content type before accessing the
     * <code>PrintWriter</code> object.
     *
     * <p>
     * The servlet container must write the headers before committing the response, because in HTTP the headers must be sent
     * before the response body.
     *
     * <p>
     * Where possible, set the Content-Length header (with the {@link ServletResponse#setContentLength}
     * method), to allow the servlet container to use a persistent connection to return its response to the client,
     * improving performance. The content length is automatically set if the entire response fits inside the response
     * buffer.
     *
     * <p>
     * When using HTTP 1.1 chunked encoding (which means that the response has a Transfer-Encoding header), do not set the
     * Content-Length header.
     *
     * <p>
     * The GET method should be safe, that is, without any side effects for which users are held responsible. For example,
     * most form queries have no side effects. If a client request is intended to change stored data, the request should use
     * some other HTTP method.
     *
     * <p>
     * The GET method should also be idempotent, meaning that it can be safely repeated. Sometimes making a method safe also
     * makes it idempotent. For example, repeating queries is both safe and idempotent, but buying a product online or
     * modifying data is neither safe nor idempotent.
     *
     * <p>
     * If the request is incorrectly formatted, <code>doGet</code> returns an HTTP "Bad Request" message.
     *
     * @param request  an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     * @param response an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     * @throws IOException      if an input or output error is detected when the servlet handles the GET request
     * @throws ServletException if the request for the GET could not be handled
     * @see ServletResponse#setContentType
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            PersonService.loadPeople(1, 50);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        List<PersonDTO> people = PersonService.getPeople();
        request.setAttribute("people", people);
        request.getRequestDispatcher("show.jsp").forward(request, response);
    }
}
