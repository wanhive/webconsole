package com.wanhive.iot.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.ClientProperties;

import com.wanhive.iot.bean.User;

/**
 * Servlet implementation class SignOutServlet
 */
@WebServlet(description = "Signs out an user", urlPatterns = { "/SignOut" })
public class SignOutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private void deleteSession(String baseUrl, String token) {
		Client client = null;
		try {
			if (token == null || token.length() == 0) {
				return;
			}
			client = ClientBuilder.newClient();
			client.target(baseUrl).path("api/user/token").property(ClientProperties.FOLLOW_REDIRECTS, Boolean.TRUE)
					.request().header("Authorization", new StringBuilder().append("Bearer ").append(token).toString())
					.delete();
		} finally {
			try {
				if (client != null)
					client.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		try {
			User user = (User) session.getAttribute("user");
			if (user != null) {
				String baseUrl = new StringBuilder().append("http://localhost:").append(request.getServerPort())
						.append(request.getContextPath()).toString();
				deleteSession(baseUrl, user.getToken());
			}
		} finally {
			session.invalidate();
			response.sendRedirect("index.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
