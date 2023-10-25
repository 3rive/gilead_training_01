/**
 * 
 */
package com.gilead.controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/**
 * 
 */
public class InitServlet implements Servlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
		System.out.print("Inside Init Servlet ok");
	}

	@Override
	public ServletConfig getServletConfig() {
		System.out.print("Inside Init Servlet config");
		return null;
	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
        PrintWriter pw = res.getWriter();
        pw.println("<h2 style=\"color:Tomato;\">From init Servlet</h2>");
        System.out.println("Done from init servlet");
	}

	@Override
	public String getServletInfo() {
		System.out.print("Inside getServletInfo of Init Servlet");
		return null;
	}

	@Override
	public void destroy() {
		System.out.print("Inside destroy method of Init Servlet");

	}

}
