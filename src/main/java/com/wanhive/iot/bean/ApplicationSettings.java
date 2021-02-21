package com.wanhive.iot.bean;

public class ApplicationSettings {
	private boolean cors;
	private int maxPasswordHashRounds;
	private int maxThingsPerDomain;
	private int maxItemsInList;
	private int minSearchKeywordLength;
	private int maxSearchKeywordLength;

	public boolean isCors() {
		return cors;
	}

	public void setCors(boolean cors) {
		this.cors = cors;
	}

	public int getMaxPasswordHashRounds() {
		return maxPasswordHashRounds;
	}

	public void setMaxPasswordHashRounds(int maxPasswordHashRounds) {
		this.maxPasswordHashRounds = maxPasswordHashRounds;
	}

	public int getMaxThingsPerDomain() {
		return maxThingsPerDomain;
	}

	public void setMaxThingsPerDomain(int maxThingsPerDomain) {
		this.maxThingsPerDomain = maxThingsPerDomain;
	}

	public int getMaxItemsInList() {
		return maxItemsInList;
	}

	public void setMaxItemsInList(int maxItemsInList) {
		this.maxItemsInList = maxItemsInList;
	}

	public int getMinSearchKeywordLength() {
		return minSearchKeywordLength;
	}

	public void setMinSearchKeywordLength(int minSearchKeywordLength) {
		this.minSearchKeywordLength = minSearchKeywordLength;
	}

	public int getMaxSearchKeywordLength() {
		return maxSearchKeywordLength;
	}

	public void setMaxSearchKeywordLength(int maxSearchKeywordLength) {
		this.maxSearchKeywordLength = maxSearchKeywordLength;
	}
}
