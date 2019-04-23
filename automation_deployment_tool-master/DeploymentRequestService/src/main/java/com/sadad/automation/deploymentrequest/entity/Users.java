package com.sadad.automation.deploymentrequest.entity;

public class Users {

	private String userId;
	private String displayName;
	private String email;
	private String groups;

	public Users(String userId, String displayName, String email, String groups) {
		super();
		this.userId = userId;
		this.displayName = displayName;
		this.email = email;
		this.groups = groups;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}

}
