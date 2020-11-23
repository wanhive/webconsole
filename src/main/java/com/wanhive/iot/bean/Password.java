package com.wanhive.iot.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Password {
	String password;
	int rounds;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getRounds() {
		return rounds;
	}
	public void setRounds(int rounds) {
		this.rounds = rounds;
	}
}
