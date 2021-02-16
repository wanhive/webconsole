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
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientProperties;
import com.wanhive.iot.bean.User;
import com.wanhive.iot.util.Role;

/**
 * Servlet implementation class AuthenticationServlet
 */
@WebServlet(description = "Authenticates a login request", urlPatterns = { "/Authenticate" })
public class AuthenticationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private User getToken(String baseUrl, String username, String password, String captcha) {
		Client client = null;
		Response response = null;
		User user = null;
		try {
			User subject = new User();
			subject.setEmail(username);
			subject.setPassword(password);
			subject.setCaptcha(captcha);

			client = ClientBuilder.newClient();
			response = client.target(baseUrl).path("api/authenticate")
					.property(ClientProperties.FOLLOW_REDIRECTS, Boolean.TRUE).request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(subject, MediaType.APPLICATION_JSON));

			if (response.getStatus() == Response.Status.OK.getStatusCode()) {
				user = response.readEntity(User.class);
			}
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (Exception e) {
			}
			try {
				if (client != null)
					client.close();
			} catch (Exception e) {
			}
		}

		return user;
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AuthenticationServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String captcha = request.getParameter("captcha");
		User user = getToken(new StringBuilder().append("http://localhost:").append(request.getServerPort())
				.append(request.getContextPath()).toString(), username, password, captcha);
		if (user != null) {
			session.setAttribute("user", user);
			if (user.getType() == Role.ADMINISTRATOR.getValue()) {
				response.sendRedirect("users.jsp");
			} else {
				response.sendRedirect("domain.jsp");
			}
		} else {
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
