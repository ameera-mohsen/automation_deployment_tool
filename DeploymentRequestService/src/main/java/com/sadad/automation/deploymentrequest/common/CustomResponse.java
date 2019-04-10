package com.sadad.automation.deploymentrequest.common;

import java.util.List;

public class CustomResponse {

	private Status responseStatus;
	private Object responseBody;
	@SuppressWarnings("unused")
	private List<Object> listResponseBody;


	public CustomResponse() {
		
	}
	
	public CustomResponse(Status responseStatus) {
		super();
		this.responseStatus = responseStatus;
	}
	
	
	public CustomResponse(Status responseStatus, Object responseBody) {
		super();
		this.responseStatus = responseStatus;
		this.responseBody = responseBody;
	}
	
	public CustomResponse(Status responseStatus, List<Object> listResponseBody) {
		super();
		this.responseStatus = responseStatus;
		this.listResponseBody = listResponseBody;
	}
	
	
	@Override
	public String toString() {
		return "CustomResponse [responseStatus=" + responseStatus + ", responseBody=" + responseBody + "]";
	}

	public Status getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(Status responseStatus) {
		this.responseStatus = responseStatus;
	}

	public Object getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(Object responseBody) {
		this.responseBody = responseBody;
	}


	
	

}
