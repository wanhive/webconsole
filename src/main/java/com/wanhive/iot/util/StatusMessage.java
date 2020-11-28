package com.wanhive.iot.util;

import com.wanhive.iot.bean.Status;

public class StatusMessage {
	public static final String S_OK = "ok";
	public static final String S_NOK = "fail";

	public static final String M_DENIED = "request denied";
	public static final String M_PROCESSED = "processed";
	public static final String M_CREATED = "created";
	public static final String M_UPDATED = "updated";
	public static final String M_DELETED = "deleted";

	public static final Status DENIED = new Status(S_NOK, M_DENIED);
	public static final Status PROCESSED = new Status(S_OK, M_PROCESSED);
	public static final Status CREATED = new Status(S_OK, M_CREATED);
	public static final Status UPDATED = new Status(S_OK, M_UPDATED);
	public static final Status DELETED = new Status(S_OK, M_DELETED);
}
