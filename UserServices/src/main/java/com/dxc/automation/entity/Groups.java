package com.dxc.automation.entity;

import org.springframework.data.mongodb.core.mapping.Field;

public class Groups {

	private int id;
	@Field("name")
	private String groupNames;

	public Groups(int id, String groupNames) {
		super();
		this.id = id;
		this.groupNames = groupNames;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(String groupNames) {
		this.groupNames = groupNames;
	}

}
