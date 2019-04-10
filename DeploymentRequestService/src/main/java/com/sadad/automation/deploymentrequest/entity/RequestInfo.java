package com.sadad.automation.deploymentrequest.entity;

public class RequestInfo {

	private String userId;
	private String displayName;
	private String comment;
	private String time;

	public RequestInfo() {

	}

	public RequestInfo(String userId, String displayName, String comment, String time) {
		super();
		this.userId = userId;
		this.displayName = displayName;
		this.comment = comment;
		this.time = time;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
