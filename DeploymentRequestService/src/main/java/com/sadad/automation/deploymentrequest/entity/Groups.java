package com.sadad.automation.deploymentrequest.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Groups")
public class Groups {

	@Id
	private String _id;
	private String groupNames;

	public Groups(String _id, String groupNames) {
		super();
		this._id = _id;
		this.groupNames = groupNames;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(String groupNames) {
		this.groupNames = groupNames;
	}

}
