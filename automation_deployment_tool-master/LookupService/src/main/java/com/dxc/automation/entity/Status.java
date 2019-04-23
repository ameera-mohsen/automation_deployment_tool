package com.dxc.automation.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Status")
public class Status {

	@Id
	private String id;
	@Field("name")
	private String statusName;
	@Field("details")
	private String statusDetails;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getStatusDetails() {
		return statusDetails;
	}

	public void setStatusDetails(String statusDetails) {
		this.statusDetails = statusDetails;
	}

}
