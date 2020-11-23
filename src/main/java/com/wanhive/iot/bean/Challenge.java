package com.wanhive.iot.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Challenge {
	private String challenge;
	private String context;
	private String secret;
	private String captcha;
	public String getChallenge() {
		return challenge;
	}
	public void setChallenge(String challenge) {
		this.challenge = challenge;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getCaptcha() {
		return captcha;
	}
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
}
