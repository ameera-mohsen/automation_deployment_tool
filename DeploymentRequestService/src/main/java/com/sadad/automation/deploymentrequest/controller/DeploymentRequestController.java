package com.sadad.automation.deploymentrequest.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.sadad.automation.deploymentrequest.common.CustomResponse;
import com.sadad.automation.deploymentrequest.common.Status;
import com.sadad.automation.deploymentrequest.entity.DeploymentRequest;
import com.sadad.automation.deploymentrequest.entity.RequestInfo;
import com.sadad.automation.deploymentrequest.entity.Users;
import com.sadad.automation.deploymentrequest.service.DeploymentRequestService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class DeploymentRequestController {

	@Autowired
	private DeploymentRequestService deploymentRequestService;
	private static final Logger LOGGER = LoggerFactory.getLogger(DeploymentRequestController.class);

	@GetMapping("/DeploymentRequests")
	private ResponseEntity<CustomResponse> findAllDeploymentRequests() {
		List<DeploymentRequest> deploymentReq = deploymentRequestService.listDeploymentRequests();
		return deploymentRequestService.buildSuccessListResponse(deploymentReq,
				" Deployment Request Retrived Successfully..");
	}

	@GetMapping("/DeploymentRequestById/{deploymentReqId}")
	private ResponseEntity<CustomResponse> findDeploymentRequestById(@PathVariable String deploymentReqId) {
		DeploymentRequest request = deploymentRequestService.findById(deploymentReqId);
		return deploymentRequestService.buildSuccessResponse(request, " Deployment Request Retrived Successfully..");

	}

	@GetMapping("/DeploymentReqByInitiatorUserId/{userId}")
	private ResponseEntity<CustomResponse> findDeploymentReqByInitiatorUserId(@PathVariable String userId) {
		List<DeploymentRequest> request = deploymentRequestService.findDeploymentReqByInitiatorUserId(userId);
		return deploymentRequestService.buildSuccessListResponse(request,
				" Deployment Request Retrived Successfully..");

	}
	
	@GetMapping("/AllowedStatusesList/{currentStatus}/{assignedGroup}")
	private List<String> getAllowedStatusesList(@PathVariable String currentStatus, @PathVariable String assignedGroup){	
		return deploymentRequestService.getAllowedStatusesList(currentStatus, assignedGroup);
	}

	@GetMapping("/DeploymentReqByAssignedUserId/{userId}")
	private ResponseEntity<CustomResponse> findDeploymentReqByAssignedUserId(@PathVariable String userId) {
		List<DeploymentRequest> request = deploymentRequestService.findDeploymentReqByAssignedUserId(userId);
		return deploymentRequestService.buildSuccessListResponse(request,
				" Deployment Request Retrived Successfully..");
	}

	@PostMapping("/addDeploymentRequest")
	private ResponseEntity<CustomResponse> addDeploymentRequest(@RequestBody DeploymentRequest deploymentRequest) {
		System.err.println("In addDeploymentRequest --- ");
		DeploymentRequest request = deploymentRequestService.addDeploymentRequest(deploymentRequest);
		return deploymentRequestService.buildSuccessResponse(request,
				" Deployment Request Created and Assigned Successfully..");

	}

	@PutMapping("/updateDeploymentRequest")
	public ResponseEntity<CustomResponse> updateDeploymentRequest(@RequestBody DeploymentRequest deploymentRequest) {
		DeploymentRequest request = deploymentRequestService.updateDeploymentRequest(deploymentRequest);
		return deploymentRequestService.buildSuccessResponse(request, " Deployment Request Updated Successfully..");
	}
	
	// TODO
	@PutMapping("/DeploymentRequestPicked/{deploymentReqId}")
	public ResponseEntity<CustomResponse> deploymentRequestPicked(@PathVariable String deploymentReqId,
			@RequestBody Users pickedByUser) {
		DeploymentRequest deploymentRequest = deploymentRequestService.updateDeploymentOnPickUp(deploymentReqId,
				pickedByUser);
		return deploymentRequestService.buildSuccessResponse(deploymentRequest,
				" Deployment Request Marked Picked Successfully..");
	}

	@PutMapping("/UpdateDeploymentRequestStatus/{deploymentReqId}/{newStatus}/{deploymentTime}")
	public ResponseEntity<CustomResponse> updateDeploymentRequestStatus(@PathVariable String deploymentReqId,
			@PathVariable String newStatus,
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd-HH:mm:ss") Date deploymentTime) {
		DeploymentRequest deploymentRequest = deploymentRequestService.updateDeploymentStatus(deploymentReqId,
				newStatus, deploymentTime);
		return deploymentRequestService.buildSuccessResponse(deploymentRequest,
				" Deployment Status Updated, Postponed and Assigned Successfully..");

	}

	@PutMapping("/UpdateDeploymentRequestStatus/{deploymentReqId}/{newStatus}")
	public ResponseEntity<CustomResponse> updateDeploymentRequestStatus(@PathVariable String deploymentReqId,
			@PathVariable String newStatus) {
		DeploymentRequest deploymentRequest = deploymentRequestService.updateDeploymentStatus(deploymentReqId,
				newStatus, new Date());
		return deploymentRequestService.buildSuccessResponse(deploymentRequest,
				" Deployment Status Updated and Assigned Successfully..");

	}
	
	@PutMapping("/UpdateDeploymentRequestStatusSubject/{deploymentReqId}/{newStatus}/{requestSubject}")
	public ResponseEntity<CustomResponse> updateDeploymentStatusCommentSubject(@PathVariable String deploymentReqId,
			@PathVariable String newStatus, @PathVariable String requestSubject) {
		DeploymentRequest deploymentRequest = deploymentRequestService.updateDeploymentStatusCommentSubject(deploymentReqId, newStatus, 
				new Date(), requestSubject);
		return deploymentRequestService.buildSuccessResponse(deploymentRequest,
				" Deployment Status Updated and Assigned Successfully..");

	}

	@PutMapping("/addRequestInfo/{deploymentReqId}/{newStatus}")
	public ResponseEntity<CustomResponse> addToDeploymentRequestRequestInfo(@PathVariable String deploymentReqId,
			@PathVariable String newStatus, @RequestBody RequestInfo requestInfo) {
		DeploymentRequest deploymentRequest = deploymentRequestService.addRequestInfo(requestInfo, deploymentReqId,
				newStatus);
		return deploymentRequestService.buildSuccessResponse(deploymentRequest, " Request Info Submitted.");
	}

	@DeleteMapping("/deleteDeploymentRequest{deploymentRequestId}")
	public ResponseEntity<CustomResponse> deleteDeploymentRequest(
			@PathVariable("deploymentRequestId") String deploymentRequestId) {
		deploymentRequestService.deleteDeploymentRequest(deploymentRequestId);
		return deploymentRequestService.buildSuccessResponse(deploymentRequestId, " Deleted Successfully");
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<CustomResponse> handleTodoNotFound(String emailOrId) {
		LOGGER.debug("***Handling error with message: {} " + new Date());
		return new ResponseEntity<CustomResponse>(
				new CustomResponse(new Status(500, "Fail, User " + emailOrId + " not found"), null),
				HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("searchDeploymentRequestByCriteria")
	public ResponseEntity<CustomResponse> searchDeploymentRequestByCriteria(@RequestParam MultiValueMap<String,String> searchCriteria){
		List<DeploymentRequest> deploymentRequestList = deploymentRequestService.searchDeploymentRequestByCriteria(searchCriteria);	
		return deploymentRequestService.buildSuccessListResponse(deploymentRequestList, " Deployment Request Retrived Successfully..");	
	}

}
