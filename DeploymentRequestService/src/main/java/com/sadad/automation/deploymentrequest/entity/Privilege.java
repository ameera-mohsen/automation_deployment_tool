package com.sadad.automation.deploymentrequest.entity;

public class Privilege {

	private int level;
	private String group;

	public Privilege() {
	}

	public Privilege(int level, String group) {
		super();
		this.level = level;
		this.group = group;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

}
