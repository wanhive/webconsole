package com.wanhive.iot.util;

public enum Role {
	ADMINISTRATOR(3), MAINTAINER(2), REPORTER(1), USER(0);
	private int v;
	private Role(int v) {
		this.v=v;
	}
	
	public int getValue() {
		return v;
	}
}
