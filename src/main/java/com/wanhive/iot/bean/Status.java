package com.wanhive.iot.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Status {
	String status;
	String message;
	String description;

	public Status() {

	}

	public Status(String status) {
		setStatus(status);
	}

	public Status(String status, String message) {
		setStatus(status);
		setMessage(message);
	}

	public Status(String status, String message, String description) {
		setStatus(status);
		setMessage(message);
		setDescription(description);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
