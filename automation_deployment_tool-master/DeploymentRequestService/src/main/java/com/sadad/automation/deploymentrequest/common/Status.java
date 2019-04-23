package com.sadad.automation.deploymentrequest.common;

public class Status {

	private int statusCode;
	private String statusMsg;

	public Status(int statusCode, String statusMsg) {
		super();
		this.statusCode = statusCode;
		this.statusMsg = statusMsg;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}

}
