package com.wanhive.iot.bean;

import java.time.OffsetDateTime;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Domain {
	private long uid;
	private long userUid;
	private OffsetDateTime createdOn;
	private OffsetDateTime modifiedOn;
	private String name;
	private String description;
	private int type;
	private int status;
	private int flag;
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public long getUserUid() {
		return userUid;
	}
	public void setUserUid(long userUid) {
		this.userUid = userUid;
	}
	public OffsetDateTime getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(OffsetDateTime createdOn) {
		this.createdOn = createdOn;
	}
	public OffsetDateTime getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(OffsetDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
}
