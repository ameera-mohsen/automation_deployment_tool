package com.sadad.automation.deploymentrequest.entity;

import java.util.Date;

public class PostPonedRequestInfo {
	
	String comment;
	Date time;
	String userId;
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	

}
