package com.wanhive.iot.provider;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.wanhive.iot.Constants;

public class DataSourceProvider {
	public static DataSource get() throws NamingException {
		return (DataSource) new InitialContext().lookup(Constants.getDataSourceName());
	}
}
