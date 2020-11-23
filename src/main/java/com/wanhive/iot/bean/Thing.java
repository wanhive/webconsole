package com.wanhive.iot.bean;

import java.time.OffsetDateTime;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Thing {
	private long uid;
	private long domainUid;
	private OffsetDateTime createdOn;
	private OffsetDateTime modifiedOn;
	private String name;
	private String salt;
	private String verifier;
	private int type;
	private int status;
	private int flag;
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public long getDomainUid() {
		return domainUid;
	}
	public void setDomainUid(long domainUid) {
		this.domainUid = domainUid;
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
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getVerifier() {
		return verifier;
	}
	public void setVerifier(String verifier) {
		this.verifier = verifier;
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
