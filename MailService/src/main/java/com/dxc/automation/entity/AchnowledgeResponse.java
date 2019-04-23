package com.dxc.automation.entity;

public class AchnowledgeResponse {
	
	int achnowledgeCode;
	String achnowledgeStatus;
	String achnowledgeMsg;
	
	public AchnowledgeResponse(int achnowledgeCode, String achnowledgeStatus, String achnowledgeMsg) {
		super();
		this.achnowledgeCode = achnowledgeCode;
		this.achnowledgeStatus = achnowledgeStatus;
		this.achnowledgeMsg = achnowledgeMsg;
	}
	
	public AchnowledgeResponse() {
		
	}

	public int getAchnowledgeCode() {
		return achnowledgeCode;
	}

	public void setAchnowledgeCode(int achnowledgeCode) {
		this.achnowledgeCode = achnowledgeCode;
	}

	public String getAchnowledgeStatus() {
		return achnowledgeStatus;
	}

	public void setAchnowledgeStatus(String achnowledgeStatus) {
		this.achnowledgeStatus = achnowledgeStatus;
	}

	public String getAchnowledgeMsg() {
		return achnowledgeMsg;
	}

	public void setAchnowledgeMsg(String achnowledgeMsg) {
		this.achnowledgeMsg = achnowledgeMsg;
	}
	
	

}
