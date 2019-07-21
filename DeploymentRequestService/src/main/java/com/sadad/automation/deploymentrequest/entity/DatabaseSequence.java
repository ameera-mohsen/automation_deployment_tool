package com.sadad.automation.deploymentrequest.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
@Document(collection = "DatabaseSequence")
public class DatabaseSequence {
	
	@Field("_id")
    private String ID;
	
	@Field("SequenceValue")
    private int sequenceValue;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public int getSequenceValue() {
		return sequenceValue;
	}

	public void setSequenceValue(int sequenceValue) {
		this.sequenceValue = sequenceValue;
	}

	
}
