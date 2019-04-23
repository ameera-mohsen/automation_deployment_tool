package com.sadad.automation.deploymentrequest.common;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DeploymentReqException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeploymentReqException(String exception) {
		super(exception);
	}
}
