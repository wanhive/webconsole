package com.wanhive.iot;

import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.wanhive.iot.bean.ApplicationInfo;
import com.wanhive.iot.provider.ContextLookupProvider;

public class Constants {
	private static boolean allowSignUp;
	private static String reCaptchaSecret;
	private static String reCaptchaSiteKey;
	private static String swaggerSchemes;
	private static String swaggerHost;

	private static boolean cors;
	private static String dataSourceName;
	private static String emailSessionName;
	private static int maxPasswordHashRounds;
	private static int maxThingsPerDomain;
	private static int maxItemsInList;
	private static int minSearchKeywordLength;
	private static ApplicationInfo appInfo;

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
			cors = Boolean.parseBoolean(resource.getString("cors"));
			dataSourceName = resource.getString("dataSourceName");
			emailSessionName = resource.getString("emailSessionName");
			maxPasswordHashRounds = Integer.parseInt(resource.getString("maxPasswordHashRounds"));
			maxThingsPerDomain = Integer.parseInt(resource.getString("maxThingsPerDomain"));
			maxItemsInList = Integer.parseInt(resource.getString("maxItemsInList"));
			minSearchKeywordLength = Integer.parseInt(resource.getString("minSearchKeywordLength"));

			/*
			 * Populate application information bundle
			 */
			appInfo = new ApplicationInfo();
			appInfo.setTitle(resource.getString("title"));
			appInfo.setDescription(resource.getString("description"));
			appInfo.setVersion(resource.getString("version"));
			appInfo.setYear(resource.getString("year"));

			appInfo.setAuthor(resource.getString("author"));
			appInfo.setUrl(resource.getString("url"));
			appInfo.setContact(resource.getString("contact"));

			// SUCCESS
			Logger.getGlobal().info("Application settings have been loaded");
		} catch (Exception e) {
			Logger.getGlobal().severe("Failed to load application settings: " + e.getMessage());
			e.printStackTrace();
		}

	}

	public static boolean allowCORS() {
		return cors;
	}

	public static String getDataSourceName() {
		return dataSourceName;
	}

	public static String getEmailSessionName() {
		return emailSessionName;
	}

	public static int getMaxPasswordHashRounds() {
		return maxPasswordHashRounds >= 0 ? maxPasswordHashRounds : 0;
	}

	public static int getMaxThingsPerDomain() {
		return maxThingsPerDomain >= 0 ? maxThingsPerDomain : 0;
	}

	public static int getMaxItemsInList() {
		return maxItemsInList >= 0 ? maxItemsInList : 0;
	}

	public static int getMinSearchKeywordLength() {
		return minSearchKeywordLength >= 0 ? minSearchKeywordLength : 0;
	}

	public static ApplicationInfo getInfo() {
		return appInfo;
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
