package com.dxc.automation.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Services")
public class Service {

	@Id
	private String id;
	@Field("name")
	private String serviceName;
	@Field("details")
	private String serviceDetails;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceDetails() {
		return serviceDetails;
	}

	public void setServiceDetails(String serviceDetails) {
		this.serviceDetails = serviceDetails;
	}

}
