package com.dxc.automation.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Layers")
public class Layer {

	@Id
	private String id;
	@Field("name")
	private String layerName;
	@Field("details")
	private String layerDetails;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}

	public String getLayerDetails() {
		return layerDetails;
	}

	public void setLayerDetails(String layerDetails) {
		this.layerDetails = layerDetails;
	}

}
