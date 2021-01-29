package com.wanhive.iot.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientProperties;
import com.wanhive.iot.bean.Challenge;

/**
 * Servlet implementation class ActivationServlet
 */
@WebServlet(description = "Activates an user account", urlPatterns = { "/Activate" })
public class ActivationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private boolean activate(String baseUrl, String challenge, String context, String secret, String captcha) {
		Client client = null;
		Response response = null;
		boolean success = false;
		try {
			client = ClientBuilder.newClient();
			WebTarget webTarget = client.target(baseUrl).path("api/user/activate");
			webTarget.property(ClientProperties.FOLLOW_REDIRECTS, Boolean.TRUE);
			Challenge ch = new Challenge();
			ch.setChallenge(challenge);
			ch.setContext(context);
			ch.setSecret(secret);
			ch.setCaptcha(captcha);
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			response = invocationBuilder.post(Entity.entity(ch, MediaType.APPLICATION_JSON));
			success = (response.getStatus() == Response.Status.OK.getStatusCode());
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (Exception e) {
			}
			try {
				if (client != null) {
					client.close();
				}
			} catch (Exception e) {
			}
		}
		return success;
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ActivationServlet() {
		super();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String challenge = request.getParameter("challenge");
		String context = request.getParameter("context");
		String secret = request.getParameter("new-password");
		String captcha = request.getParameter("captcha");
		if (activate(new StringBuilder().append("http://localhost:").append(request.getServerPort())
				.append(request.getContextPath()).toString(), challenge, context, secret, captcha)) {
			response.sendRedirect("index.jsp");
		} else {
			response.sendRedirect("errorpage.jsp");
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
