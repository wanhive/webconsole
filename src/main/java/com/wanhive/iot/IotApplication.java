package com.wanhive.iot;

import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;

import com.wanhive.iot.bean.ApplicationInfo;
import com.wanhive.iot.bean.ReCaptchaResponse;
import com.wanhive.iot.provider.ContextLookupProvider;

import io.swagger.jaxrs.config.BeanConfig;

@ApplicationPath("api")
public class IotApplication extends ResourceConfig {

	private void initSwagger(String[] schemes, String host, String basePath) {
		ApplicationInfo appInfo = Constants.getInfo();
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion(appInfo.getVersion());
		beanConfig.setTitle(appInfo.getTitle());
		beanConfig.setDescription(appInfo.getDescription());
		beanConfig.setContact(appInfo.getAuthor() + " (" + appInfo.getUrl() + ")");

		beanConfig.setLicense(new StringBuilder().append("Copyright (C)").append(appInfo.getYear()).append(" ")
				.append(appInfo.getAuthor()).toString());

		beanConfig.setLicenseUrl(host + "/license.jsp");
		beanConfig.setTermsOfServiceUrl(host + "/privacy.jsp");
		if (schemes != null) {
			beanConfig.setSchemes(schemes);
		}

		if (host != null) {
			beanConfig.setHost(host);
		}

		if (basePath != null) {
			beanConfig.setBasePath(basePath);
		}
		beanConfig.setResourcePackage("com.wanhive.iot.resources");
		beanConfig.setScan(true);
	}

	public IotApplication(@Context ServletContext servletContext) throws Exception {
		try {
			/*
			 * Setup web service
			 */
			packages("com.fasterxml.jackson.jaxrs.json");
			packages("com.wanhive.iot.resources");
			register(com.wanhive.iot.filter.AuthenticationFilter.class);
			register(com.wanhive.iot.filter.AuthorizationFilter.class);
			register(com.wanhive.iot.filter.CORSFilter.class);
			register(com.wanhive.iot.UncaughtException.class);
			register(io.swagger.jaxrs.listing.ApiListingResource.class);
			register(io.swagger.jaxrs.listing.SwaggerSerializers.class);

			/*
			 * Setup Swagger API documentation
			 */
			String swaggerSchemes = Constants.getSwaggerSchemes();
			String swaggerHost = Constants.getSwaggerHost();
			if (swaggerSchemes != null && swaggerHost != null) {
				initSwagger(swaggerSchemes.split(","), swaggerHost, "/api");
			}
			Logger.getGlobal().info("Application initialized.");
		} catch (Exception e) {
			Logger.getGlobal().severe("Initialization failed: " + e.getMessage());
			throw e;
		}
	}

	public static boolean verifyCaptcha(String token) {
		try {
			if (!Constants.isCaptchaEnabled()) {
				return true;
			}

			if (token == null || token.length() == 0) {
				Logger.getGlobal().warning("Invalid captcha");
				return false;
			}
			StringBuilder verifierUrl = new StringBuilder();
			verifierUrl.append("https://www.google.com/recaptcha/api/siteverify?secret=");
			verifierUrl.append(Constants.getReCaptchaSecret());
			verifierUrl.append("&response=");
			verifierUrl.append(token);

			Client client = ClientBuilder.newClient();
			WebTarget webTarget = client.target(verifierUrl.toString());

			Invocation.Builder invocationBuilder = webTarget.request();
			Response response = invocationBuilder.get();

			if (response.getStatus() == Response.Status.OK.getStatusCode()) {
				ReCaptchaResponse rcResponse = response.readEntity(ReCaptchaResponse.class);
				Logger.getGlobal().info("Captcha verified: " + rcResponse.isSuccess() + ", Score: "
						+ rcResponse.getScore() + ", Action: " + rcResponse.getAction());

				return (rcResponse.isSuccess() && rcResponse.getScore() > 0.5);
			} else {
				Logger.getGlobal().info("Captcha Error: " + response.getStatus());
				return false;
			}
		} catch (Exception e) {
			Logger.getGlobal().severe("Captcha error: " + e.getMessage());
			return false;
		}
	}

	public static void sendEmail(String destination, String subject, String text) {
		try {
			Session session = (Session) ContextLookupProvider.lookup(Constants.getEmailSessionName());
			if (session == null) {
				Logger.getGlobal().info("Email disabled");
				return;
			}

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(session.getProperty("mail.from")));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(destination));
			message.setSubject(subject);
			message.setText(text);
			Transport.send(message);
		} catch (Exception e) {
			Logger.getGlobal().severe("Email error: " + e.getMessage());
		}
	}
}
