package com.wanhive.iot.util;

import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {
	public Properties load(String name) throws Exception {
		Properties properties = new Properties();
		try (InputStream propertiesStream = getClass().getClassLoader().getResourceAsStream(name)) {
			properties.load(propertiesStream);
			return properties;
		} catch (Exception e) {
			throw e;
		}
	}
}
