package com.sadad.automation.deploymentrequest.service;

import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import com.sadad.automation.deploymentrequest.common.APICaller;
import com.sadad.automation.deploymentrequest.common.CustomResponse;
import com.sadad.automation.deploymentrequest.common.DeploymentReqException;
import com.sadad.automation.deploymentrequest.common.Status;
import com.sadad.automation.deploymentrequest.common.StatusCode;
import com.sadad.automation.deploymentrequest.entity.DeploymentRequest;
import com.sadad.automation.deploymentrequest.entity.RequestInfo;
import com.sadad.automation.deploymentrequest.entity.Users;

@org.springframework.stereotype.Service
public class DeploymentRequestServiceImpl implements DeploymentRequestService {

	@Autowired
	private MongoTemplate mongoTemplate;

	final String INITIATOR_EMAIL_BODY = "This email inform you that picked by ";
	final String INFO_EMAIL_BODY = "your Deployment Request Updated to ";
	final String ASSIGNE_EMAIL_BODY = "This email inform you that there is a waiting action from Automation Service assigned to you by ";

	@Override
	public DeploymentRequest addDeploymentRequest(DeploymentRequest deploymentRequest) {
		if(deploymentRequest != null ) {
//			String assignOnUserEmail = "" , pickedByUserEmail= "";
//			if(deploymentRequest.getAssignOnUser()!=null) {
//				 assignOnUserEmail = deploymentRequest.getAssignOnUser().getEmail();
//			}if(deploymentRequest.getPickedByUser()!=null) {
//				 pickedByUserEmail = deploymentRequest.getPickedByUser().getEmail();
//			}
			String[] list = new String[2];
			list[0] = "ameera.mohsen@dxc.com";
			list[1] = "mohamed.lotfi@dxc.com";
			APICaller.EmailAPIList(list, ASSIGNE_EMAIL_BODY + deploymentRequest.getInitiatorUser().getDisplayName(), getMailSubject(deploymentRequest));
			return mongoTemplate.insert(deploymentRequest);
		}
		return null;
	}
	
	private String getMailSubject(DeploymentRequest deploymentRequest) {
		String affectedServices = "";
		for (String str : deploymentRequest.getAffectedService()) {
			affectedServices+="<" + str + ">";
		}
		System.err.println(affectedServices);
		String mailSubject = "<S2>"+"<"+ deploymentRequest.getEnvironment() + ">" + affectedServices;
		System.err.println(mailSubject);
		return mailSubject;
	}

	@Override
	public DeploymentRequest updateDeploymentRequest(DeploymentRequest deploymentRequest) {
		return mongoTemplate.save(deploymentRequest);

	}

	@Override
	public void deleteDeploymentRequest(String deploymentRequestId) {
		mongoTemplate.findAndRemove(new Query(Criteria.where("id").is(deploymentRequestId)), DeploymentRequest.class);
	}

	@Override
	public List<DeploymentRequest> listDeploymentRequests() {
		return mongoTemplate.findAll(DeploymentRequest.class);
	}

	@Override
	public DeploymentRequest addRequestInfo(RequestInfo requestInfo, String deploymentRequestId, String newStatus) {
		DeploymentRequest deploymentReq = this.findById(deploymentRequestId);
		if (deploymentReq != null) {
			enrichDeploymentRequest(deploymentReq, newStatus, null);
			deploymentReq.getRequestInfo().add(requestInfo);
			mongoTemplate.save(deploymentReq);
			return deploymentReq;
		} else {
			throw new DeploymentReqException("Deployment Request " + deploymentRequestId + " Not Found..");
		}
	}

	@Override
	public DeploymentRequest findById(String deploymentRequestId) {
		DeploymentRequest deploymentReq = mongoTemplate.findById(deploymentRequestId, DeploymentRequest.class);
		if (deploymentReq != null) {
			return deploymentReq;
		} else {
			throw new DeploymentReqException("Deployment Request " + deploymentRequestId + " Not Found..");
		}
	}

	/*
	 * The below Operation will used when on of the testing team is select a
	 * deployment request to working on it will update deployment request with user
	 * id and make it editable just for him
	 */
	@Override
	public DeploymentRequest updateDeploymentOnPickUp(String deploymentReqId, Users pickedByUser) {
		DeploymentRequest request = this.findById(deploymentReqId);
		if (request != null) {
			request.setPickedByUser(pickedByUser);
			request.setAssignOnUser(pickedByUser);
			APICaller.EmailAPI(request.getInitiatorUser().getEmail(),
					INITIATOR_EMAIL_BODY + request.getAssignOnUser().getDisplayName(), getMailSubject(request));
			return mongoTemplate.save(request);
		} else {
			throw new DeploymentReqException("Deployment Request " + deploymentReqId + " Not Found..");
		}
	}

	/*
	 * The below Operation will used when Change the Deployment Request it will
	 * change to all status but IN_PROGRESS except the deployment Team
	 * 
	 */
	@Override
	public DeploymentRequest updateDeploymentStatus(String deploymentRequestId, String newStatus, Date deploymentTime) {
		DeploymentRequest deploymentReqToUpdated = this.findById(deploymentRequestId);
		enrichDeploymentRequest(deploymentReqToUpdated, newStatus, deploymentTime);
		//APICaller.EmailAPI(deploymentReqToUpdated.getInitiatorUser().getEmail(),
				//INFO_EMAIL_BODY + deploymentReqToUpdated.getStatus());
		return mongoTemplate.save(deploymentReqToUpdated);
	}

	@Override
	public List<DeploymentRequest> findDeploymentReqByInitiatorUserId(String userId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("initiatorUser.userId").is(userId));
		List<DeploymentRequest> deploymentRequestList = mongoTemplate.find(query, DeploymentRequest.class);
		if (deploymentRequestList.size() > 0) {
			return deploymentRequestList;
		} else {
			throw new DeploymentReqException("No initiated DeploymentRequests by this user");
		}
	}

	@Override
	public List<DeploymentRequest> findDeploymentReqByAssignedUserId(String userId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("assignOnUser.userId").is(userId));
		List<DeploymentRequest> deploymentRequestList = mongoTemplate.find(query, DeploymentRequest.class);
		if (deploymentRequestList.size() > 0) {
			return deploymentRequestList;
		} else {
			throw new DeploymentReqException("No Assigned DeploymentRequests for this user");
		}
	}

	/*
	 * Common Part
	 */
	private boolean checkStatus(String currentStatus, String newStatus) {
		boolean flag = true;
		switch (newStatus) {
		case "CANCELED":
			if (currentStatus.equals(String.valueOf(StatusCode.IN_PROGRESS))) {
				flag = false;
			}
			break;
		case "REJECTED":
			if (currentStatus.equals(String.valueOf(StatusCode.APPROVED))) {
				flag = false;
			}
			break;
		case "APPROVED":
			if (currentStatus.equals(String.valueOf(StatusCode.REJECTED))) {
				flag = false;
			}
			break;
		}
		return flag;
	}

	private void enrichDeploymentRequest(DeploymentRequest Req, String newStatus, Date deploymentTime) {
		Users intitiator = Req.getInitiatorUser()
				, fromUser = Req.getInitiatorUser();
		Users pickedBy = Req.getPickedByUser();
		String currentStatus = Req.getStatus();
		if (checkStatus(currentStatus, newStatus)) {
			Req.setStatus(newStatus);
			if (newStatus.equals(String.valueOf(StatusCode.PENDING_APPROVAL))
					||newStatus.equals(String.valueOf(StatusCode.PENDING_VERIFICATION))
					||newStatus.equals(String.valueOf(StatusCode.INFO_SUBMITTED))) {
				fromUser = Req.getAssignOnUser();
				Req.setAssignOnUser(pickedBy);
				
			} else if (
					newStatus.equals(String.valueOf(StatusCode.APPROVED))
					|| newStatus.equals(String.valueOf(StatusCode.IN_PROGRESS))) {
				fromUser = Req.getAssignOnUser();
				Req.setAssignOnUser(APICaller.UserAPI());
			}
			 else if (newStatus.equals(String.valueOf(StatusCode.COMPLETED))
					 ||newStatus.equals(String.valueOf(StatusCode.INFO_REQUESTED))
					|| newStatus.equals(String.valueOf(StatusCode.REJECTED))) {
				fromUser = Req.getAssignOnUser();
				Req.setAssignOnUser(intitiator);
			 }
			
			Req.setDeploymentTime(deploymentTime);
			APICaller.EmailAPI(Req.getAssignOnUser().getEmail(), ASSIGNE_EMAIL_BODY + fromUser.getDisplayName(), getMailSubject(Req));
			Req.setAssignOnGroup(enrichAssignOnGroup(newStatus));
			
		}
	}

	private String enrichAssignOnGroup(String status) {
		String group = "N/A";
		switch (status) {
		case "APPROVED":
			group = "DEPLOYMENT";
			break;
		case "REJECTED":
			group = "DEVELOPMENT";
			break;
		case "PENDING_APPROVAL":
			group = "DEPLOYMENT";
			break;
		case "PENDING_VERIFICATION":
			group = "DEPLOYMENT";
			break;
		case "INFO_REQUESTED":
			group = "DEPLOYMENT";
			break;
		case "INFO_SUBMITTED":
			group = "TESTING";
			break;
		case "COMPLETED":
			group = "DEVELOPMENT";
			break;
		case "IN_PROGRESS":
			group = "DEPLOYMENT";
			break;
		case "POST_PONED":
			group = "DEPLOYMENT";
			break;
		}
		return group;
	}

	@Override
	public ResponseEntity<CustomResponse> buildSuccessResponse(DeploymentRequest Request, String msg) {
		return new ResponseEntity<CustomResponse>(new CustomResponse(new Status(200, msg), Request), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<CustomResponse> buildFailiarResponse() {
		return new ResponseEntity<CustomResponse>(new CustomResponse(new Status(500, "Fail")),
				HttpStatus.EXPECTATION_FAILED);
	}

	@Override
	public ResponseEntity<CustomResponse> buildSuccessListResponse(List<DeploymentRequest> Request, String msg) {
		return new ResponseEntity<CustomResponse>(new CustomResponse(new Status(200, msg), Request), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<CustomResponse> buildSuccessResponse(String requestId, String msg) {
		return new ResponseEntity<CustomResponse>(new CustomResponse(new Status(200, msg), requestId), HttpStatus.OK);
	}
	
	
	@Override
	public List<DeploymentRequest> searchDeploymentRequestByCriteria(MultiValueMap<String,String> searchCriteria) {
		Query query = new Query();
		 for (Entry<String, List<String>> entry : searchCriteria.entrySet()) {
			 if(entry.getKey().equals("requestDateFrom")) {
				 query.addCriteria(Criteria.where("requestDate").gte(entry.getValue()).lt(searchCriteria.get("requestDateTo")));
			 }else if (entry.getKey().equals("requestDateTo")) {
				continue;
			 }else {
				 query.addCriteria(Criteria.where(entry.getKey()).in(entry.getValue()));  
			 }
		 }
		 System.out.println("query:" + query);
		 List<DeploymentRequest> deploymentRequestList = mongoTemplate.find(query, DeploymentRequest.class);
		return deploymentRequestList;
	}

}