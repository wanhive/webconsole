package com.wanhive.iot;

import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.wanhive.iot.bean.ApplicationInfo;
import com.wanhive.iot.bean.ApplicationSettings;
import com.wanhive.iot.provider.ContextLookupProvider;

public class Constants {
	private static boolean allowSignUp;
	private static String reCaptchaSecret;
	private static String reCaptchaSiteKey;
	private static String swaggerSchemes;
	private static String swaggerHost;

	private static String dataSourceName;
	private static String emailSessionName;
	
	private static ApplicationSettings settings;
	private static ApplicationInfo info;

	static {
		try {
			// START
			Logger.getGlobal().info("Loading application settings");
			/*
			 * Read web application context and settings
			 */
			allowSignUp = Boolean
					.parseBoolean((String) ContextLookupProvider.lookup("java:comp/env/whwebc-allowsignup"));
			reCaptchaSecret = (String) ContextLookupProvider.lookup("java:comp/env/whwebc-captchasecret");
			reCaptchaSiteKey = (String) ContextLookupProvider.lookup("java:comp/env/whwebc-captchasitekey");
			swaggerSchemes = (String) ContextLookupProvider.lookup("java:comp/env/whwebc-schemes");
			swaggerHost = (String) ContextLookupProvider.lookup("java:comp/env/whwebc-hostname");

			/*
			 * Read properties
			 */
			ResourceBundle resource = ResourceBundle.getBundle("app");

			/*
			 * Populate private settings
			 */
			dataSourceName = resource.getString("dataSourceName");
			emailSessionName = resource.getString("emailSessionName");

			/*
			 * Populate public settings
			 */
			settings = new ApplicationSettings();
			settings.setCors(Boolean.parseBoolean(resource.getString("cors")));
			settings.setMaxPasswordHashRounds(Integer.parseInt(resource.getString("maxPasswordHashRounds")));
			settings.setMaxThingsPerDomain(Integer.parseInt(resource.getString("maxThingsPerDomain")));
			settings.setMaxItemsInList(Integer.parseInt(resource.getString("maxItemsInList")));
			settings.setMinSearchKeywordLength(Integer.parseInt(resource.getString("minSearchKeywordLength")));
			settings.setMaxSearchKeywordLength(Integer.parseInt(resource.getString("maxSearchKeywordLength")));

			/*
			 * Populate application information
			 */
			info = new ApplicationInfo();
			info.setTitle(resource.getString("title"));
			info.setDescription(resource.getString("description"));
			info.setVersion(resource.getString("version"));
			info.setYear(resource.getString("year"));

			info.setAuthor(resource.getString("author"));
			info.setUrl(resource.getString("url"));
			info.setContact(resource.getString("contact"));

			// SUCCESS
			Logger.getGlobal().info("Application settings have been loaded");
		} catch (Exception e) {
			Logger.getGlobal().severe("Failed to load application settings: " + e.getMessage());
			e.printStackTrace();
		}

	}

	public static String getDataSourceName() {
		return dataSourceName;
	}

	public static String getEmailSessionName() {
		return emailSessionName;
	}

	public static ApplicationSettings getSettings() {
		return settings;
	}

	public static ApplicationInfo getInfo() {
		return info;
	}

	public static boolean isSignUpAllowed() {
		return allowSignUp;
	}

	public static String getReCaptchaSecret() {
		return reCaptchaSecret;
	}

	public static String getReCaptchaSiteKey() {
		return reCaptchaSiteKey;
	}

	public static boolean isCaptchaEnabled() {
		return (reCaptchaSecret != null && reCaptchaSecret.length() > 0);
	}

	public static String getSwaggerSchemes() {
		return swaggerSchemes;
	}

	public static String getSwaggerHost() {
		return swaggerHost;
	}
}
