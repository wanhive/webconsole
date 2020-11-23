package com.wanhive.iot.provider;

import java.util.logging.Logger;

import javax.naming.InitialContext;

public class ContextLookupProvider {
	public static Object lookup(String name) {
		try {
			return new InitialContext().lookup(name);
		} catch (Exception e) {
			Logger.getGlobal().warning(e.getMessage());
			return null;
		}
	}
}
