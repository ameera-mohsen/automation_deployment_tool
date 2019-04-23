package com.dxc.automation.entity;

import org.springframework.data.mongodb.core.mapping.Field;

public class Credentials {

	@Field("email")
	private String email;
	@Field("username")
	private String userName;
	@Field("password")
	private String userPassword;

	public Credentials(String email, String userName, String userPassword) {
		super();
		this.email = email;
		this.userName = userName;
		this.userPassword = userPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

}
