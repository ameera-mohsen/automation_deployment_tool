package com.dxc.automation.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Enviroments")
public class Enviroment {

	@Id
	private String id;
	@Field("name")
	private String envName;
	@Field("details")
	private String envDetails;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

	public String getEnvDetails() {
		return envDetails;
	}

	public void setEnvDetails(String envDetails) {
		this.envDetails = envDetails;
	}

}
