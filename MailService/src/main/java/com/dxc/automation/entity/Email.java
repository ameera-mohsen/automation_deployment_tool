package com.dxc.automation.entity;

public class Email {

	private String to;
	private String cc;
	private String subject;
	private String body;
	private String[] toList;
	public String[] getToList() {
		return toList;
	}
	public void setToList(String[] toList) {
		this.toList = toList;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Email(String to, String cc, String subject, String body) {
		super();
		this.to = to;
		this.cc = cc;
		this.subject = subject;
		this.body = body;
	}
	
	public Email() {
		
	}
	
	
}
