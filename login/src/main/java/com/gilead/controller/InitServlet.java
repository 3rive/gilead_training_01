/**
 * 
 */
package com.gilead.controller;

import java.io.IOException;

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
		System.out.print("Inside Init Servlet");
	}

	@Override
	public ServletConfig getServletConfig() {
		System.out.print("Inside Init Servlet config");
		return null;
	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		System.out.print("Inside service method of init Servlet");

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
