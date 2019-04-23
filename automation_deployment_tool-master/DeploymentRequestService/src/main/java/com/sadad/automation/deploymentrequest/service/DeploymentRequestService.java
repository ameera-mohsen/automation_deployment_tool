package com.sadad.automation.deploymentrequest.service;

import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import com.sadad.automation.deploymentrequest.common.CustomResponse;
import com.sadad.automation.deploymentrequest.entity.DeploymentRequest;
import com.sadad.automation.deploymentrequest.entity.RequestInfo;
import com.sadad.automation.deploymentrequest.entity.Users;

public interface DeploymentRequestService {

	DeploymentRequest addDeploymentRequest(DeploymentRequest deploymentRequest);

	DeploymentRequest updateDeploymentRequest(DeploymentRequest deploymentRequest);

	void deleteDeploymentRequest(String deploymentRequestId);

	DeploymentRequest findById(String deploymentRequestId);

	List<DeploymentRequest> findDeploymentReqByInitiatorUserId(String userId);

	List<DeploymentRequest> findDeploymentReqByAssignedUserId(String userId);

	List<DeploymentRequest> listDeploymentRequests();

	DeploymentRequest updateDeploymentOnPickUp(String deploymentReqId, Users pickedByUser);

	DeploymentRequest updateDeploymentStatus(String deploymentReqId, String status, Date deploymentTime);

	DeploymentRequest addRequestInfo(RequestInfo requestInfo, String deploymentRequestId, String newStatus);

	ResponseEntity<CustomResponse> buildSuccessResponse(DeploymentRequest request, String msg);

	ResponseEntity<CustomResponse> buildSuccessResponse(String requestId, String msg);

	ResponseEntity<CustomResponse> buildSuccessListResponse(List<DeploymentRequest> request, String msg);

	ResponseEntity<CustomResponse> buildFailiarResponse();
	
	List<DeploymentRequest> searchDeploymentRequestByCriteria(MultiValueMap<String,String> searchCriteria);

}
