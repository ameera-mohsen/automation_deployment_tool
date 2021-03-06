package com.sadad.automation.deploymentrequest.service;

import java.util.ArrayList;
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
import com.sadad.automation.deploymentrequest.entity.DatabaseSequence;
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

	final String DEPLOYMENT_TEAM = "DEPLOYMENT";
	final String DEVELOPMENT_TEAM = "DEVELOPMENT";
	final String TESTING_TEAM = "TESTING";
	
	final String AMS_MAIL = "SADAD_AMS@dxc.com";
	
	@Override
	public DeploymentRequest addDeploymentRequest(DeploymentRequest deploymentRequest) {
		if (deploymentRequest != null) {
			String assignOnUserEmail = "", pickedByUserEmail = "";
			if (deploymentRequest.getAssignOnUser() != null) {
				assignOnUserEmail = deploymentRequest.getAssignOnUser().getEmail();
			}
			if (deploymentRequest.getPickedByUser() != null) {
				pickedByUserEmail = deploymentRequest.getPickedByUser().getEmail();
			}
			String[] list = new String[2];
			list[0] = assignOnUserEmail;
			list[1] = pickedByUserEmail;

			String deploymentSubject = initMailSubject(deploymentRequest);
			APICaller.EmailAPIList(list, ASSIGNE_EMAIL_BODY + deploymentRequest.getInitiatorUser().getDisplayName(), deploymentSubject);

			deploymentRequest.setRequestSubject(deploymentSubject);
			String id;
			try {
				id = (String) getNextSequence();
			}
			catch (Exception e) {
				id = "temp";
				System.out.println("Exception is -- " + e.getMessage());
			}
			
			deploymentRequest.setId(id);
			System.out.println("Befor insert in DB");
			return mongoTemplate.insert(deploymentRequest);
		}
		return null;
	}

	
	private String initMailSubject(DeploymentRequest deploymentRequest) {
		if(deploymentRequest.getRequestSubject()==null || deploymentRequest.getRequestSubject().isEmpty())
		{
			//In case off Add new request
			String affectedServices = "";
			for (String str : deploymentRequest.getAffectedService()) {
				affectedServices+="<" + str + ">";
			}
			System.err.println(affectedServices);
			String mailSubject = "<S2>"+"<"+ deploymentRequest.getEnvironment() + ">" + affectedServices;
			System.err.println(mailSubject);
			return mailSubject;
		}
		else {
			//In case of update request
			return deploymentRequest.getRequestSubject();
		}
	}
	@Override
	public DeploymentRequest updateDeploymentRequest(DeploymentRequest deploymentRequest) {
		DeploymentRequest deploymentReqToUpdated = this.findById(deploymentRequest.getId());
		enrichDeploymentRequest(deploymentReqToUpdated, deploymentRequest.getStatus(), deploymentReqToUpdated.getDeploymentTime());
		deploymentReqToUpdated.setRequestInfo(deploymentRequest.getRequestInfo());
		APICaller.EmailAPI(deploymentReqToUpdated.getAssignOnUser().getEmail(), ASSIGNE_EMAIL_BODY + deploymentReqToUpdated.getInitiatorUser().getDisplayName(), deploymentRequest.getRequestSubject());
		//Notify DEPLOYMENT with REJECTED status 
		if(deploymentReqToUpdated.getStatus().equals(String.valueOf(StatusCode.REJECTED))) {
			APICaller.EmailAPI(AMS_MAIL, "This is to notify you that request id " + deploymentReqToUpdated.getId() + " has been rejected by " 
		+ deploymentReqToUpdated.getInitiatorUser().getDisplayName(), deploymentRequest.getRequestSubject());
		}
		return mongoTemplate.save(deploymentReqToUpdated);

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
					INITIATOR_EMAIL_BODY + request.getAssignOnUser().getDisplayName(), initMailSubject(request));
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
		System.err.println(deploymentRequestId);
		DeploymentRequest deploymentReqToUpdated = this.findById(deploymentRequestId);
		System.err.println("after------------'");
		enrichDeploymentRequest(deploymentReqToUpdated, newStatus, deploymentTime);
		// APICaller.EmailAPI(deploymentReqToUpdated.getInitiatorUser().getEmail(),
		// INFO_EMAIL_BODY + deploymentReqToUpdated.getStatus());
		System.err.println("before save----'");
		return mongoTemplate.save(deploymentReqToUpdated);
	}

	@Override
	public DeploymentRequest updateDeploymentStatusCommentSubject(String deploymentReqId, String newStatus,
			Date deploymentTime, String requestSubject) {
		System.err.println(deploymentReqId);
		DeploymentRequest deploymentReqToUpdated = this.findById(deploymentReqId);
		enrichDeploymentRequest(deploymentReqToUpdated, newStatus, deploymentTime);
//		APICaller.EmailAPI(deploymentReqToUpdated.getInitiatorUser().getEmail(),
//				INFO_EMAIL_BODY + deploymentReqToUpdated.getStatus());
		System.err.println("before save----'");
		deploymentReqToUpdated.setRequestSubject(requestSubject);
		APICaller.EmailAPI(deploymentReqToUpdated.getAssignOnUser().getEmail(), ASSIGNE_EMAIL_BODY + deploymentReqToUpdated.getInitiatorUser().getDisplayName(), requestSubject);
		// APICaller.EmailAPI(deploymentReqToUpdated.getInitiatorUser().getEmail(),
		// INFO_EMAIL_BODY + deploymentReqToUpdated.getStatus());
		System.err.println("before save----'");
		deploymentReqToUpdated.setRequestSubject(requestSubject);
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
		Users intitiator = Req.getInitiatorUser(), fromUser = Req.getInitiatorUser();
		Users pickedBy = Req.getPickedByUser();
		String currentStatus = Req.getStatus();
		System.err.println("currentStatus------------'" + currentStatus);
		System.err.println("NewStatus------------'" + newStatus);
		System.err.println("pickedBy------------'" + pickedBy.getDisplayName());
		System.err.println("intitiator------------'" + intitiator.getDisplayName());
		if (checkStatus(currentStatus, newStatus)) {
			Req.setStatus(newStatus);
			if (newStatus.equals(String.valueOf(StatusCode.PENDING_APPROVAL))
					|| newStatus.equals(String.valueOf(StatusCode.POSTPONED))
					|| newStatus.equals(String.valueOf(StatusCode.VERIFIED))
					|| newStatus.equals(String.valueOf(StatusCode.COMPLETED))) {
				//SIT
				fromUser = Req.getAssignOnUser();
				Req.setAssignOnUser(pickedBy);

			} else if (newStatus.equals(String.valueOf(StatusCode.APPROVED))
					|| newStatus.equals(String.valueOf(StatusCode.IN_PROGRESS))
					|| newStatus.equals(String.valueOf(StatusCode.SENT))) {
				//DEPLOYMENT
				fromUser = Req.getAssignOnUser();
				Req.setAssignOnUser(APICaller.UserAPI());
			} else if (newStatus.equals(String.valueOf(StatusCode.INFO_REQUESTED))
					|| newStatus.equals(String.valueOf(StatusCode.CANCELED))
					|| newStatus.equals(String.valueOf(StatusCode.REJECTED))
					|| newStatus.equals(String.valueOf(StatusCode.PENDING_VERIFICATION))) {
				//DEV
				fromUser = Req.getAssignOnUser();
				Req.setAssignOnUser(intitiator);
			}

			Req.setDeploymentTime(deploymentTime);
			System.err.println("after setting deployment time");
			//APICaller.EmailAPI(Req.getAssignOnUser().getEmail(), ASSIGNE_EMAIL_BODY + fromUser.getDisplayName(),
				//	initMailSubject(Req));
			Req.setAssignOnGroup(enrichAssignOnGroup(newStatus));
			System.err.println(Req.getAssignOnGroup());
		}
	}

	private String enrichAssignOnGroup(String status) {
		String group = "N/A";
		switch (status) {
		case "APPROVED":
		case "SENT":
		case "IN_PROGRESS":
			group = "DEPLOYMENT";
			break;
			//AND
			//group = "DEVELOPMENT";
			//break;
		case "REJECTED":
		case "INFO_REQUESTED":
		case "CANCELED":
		case "PENDING_VERIFICATION":
			group = "DEVELOPMENT";
			break;
		//case "PENDING_APPROVAL":
			//group = "DEPLOYMENT";
			//break;
		/*
		 * case "INFO_REQUESTED": group = "DEPLOYMENT"; break;
		 */
		case "INFO_SUBMITTED":
		case "PENDING_CANCEL":
		case "PENDING_APPROVAL":
		case "POSTPONED":
		case "VERIFIED":
		case "COMPLETED":
			group = "TESTING";
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
	public List<DeploymentRequest> searchDeploymentRequestByCriteria(MultiValueMap<String, String> searchCriteria) {
		Query query = new Query();

		for (Entry<String, List<String>> entry : searchCriteria.entrySet()) {
			if (entry.getKey().equals("requestDateFrom")) {
				query.addCriteria(
						Criteria.where("requestDate").gte(entry.getValue()).lt(searchCriteria.get("requestDateTo")));
			} else if (entry.getKey().equals("requestDateTo")) {
				continue;
			} else if (entry.getKey().equals("initiatorUser.displayName")) {
				query.addCriteria(Criteria.where(entry.getKey()).in(entry.getValue()));
				//query.addCriteria(Criteria.where(entry.getKey()).regex("^"+entry.getValue().toString()));

			} else {
				query.addCriteria(Criteria.where(entry.getKey()).in(entry.getValue()));
			}
		}
		System.out.println("query:" + query);
		List<DeploymentRequest> deploymentRequestList = mongoTemplate.find(query, DeploymentRequest.class);
		return deploymentRequestList;
	}

	public Object getNextSequence() {
		DatabaseSequence databaseSequence = mongoTemplate.findById("requestid",DatabaseSequence.class);
		//System.out.println("databaseSequence:" + databaseSequence.getID() +"   "+databaseSequence.getSequenceValue());
		int seq= databaseSequence.getSequenceValue() ;
		databaseSequence.setSequenceValue(seq+1);
		mongoTemplate.save(databaseSequence);
		return String.valueOf(databaseSequence.getSequenceValue());
	}
	
	@Override
	public List<String> getAllowedStatusesList(String currentStatus, String assigedGroup) {
		List<String> resultList = new ArrayList<String>();
		switch (StatusCode.valueOf(currentStatus)) {
		case SENT:
			switch (assigedGroup) {
			case DEPLOYMENT_TEAM:
				resultList.add(String.valueOf(StatusCode.PENDING_APPROVAL));
				resultList.add(String.valueOf(StatusCode.INFO_REQUESTED));	
				break;
			default:
				resultList.add(currentStatus);
				break;
			}
			break;
			
		case PENDING_APPROVAL:
			switch (assigedGroup) {
			case TESTING_TEAM:
				resultList.add(String.valueOf(StatusCode.APPROVED));
				resultList.add(String.valueOf(StatusCode.REJECTED));	
				resultList.add(String.valueOf(StatusCode.POSTPONED));	
				resultList.add(String.valueOf(StatusCode.INFO_REQUESTED));	
				break;
			default:
				 resultList.add(currentStatus);
				 break;
			}		
			break;
			
		case INFO_REQUESTED:
			switch (assigedGroup) {
			case DEVELOPMENT_TEAM:
				resultList.add(String.valueOf(StatusCode.SENT));
				resultList.add(String.valueOf(StatusCode.CANCELED));
				break;
			default:
				resultList.add(currentStatus);
				break;
			}
			break;
			
		case APPROVED:
			switch (assigedGroup) {
			case DEPLOYMENT_TEAM:
				resultList.add(String.valueOf(StatusCode.IN_PROGRESS));	
				break;
			default:
				resultList.add(currentStatus);
				break;
			}
			break;
			
		case REJECTED:
			switch (assigedGroup) {
			case DEVELOPMENT_TEAM:
				resultList.add(String.valueOf(StatusCode.REJECTED));	
				break;
			}
			break;
		case POSTPONED:
			switch (assigedGroup) {
			case TESTING_TEAM:
				resultList.add(String.valueOf(StatusCode.APPROVED));	
				resultList.add(String.valueOf(StatusCode.REJECTED));	
				break;
			default:
				resultList.add(currentStatus);
				break;
			}
			break;
		case IN_PROGRESS:
			switch (assigedGroup) {
			case DEPLOYMENT_TEAM:
				resultList.add(String.valueOf(StatusCode.PENDING_VERIFICATION));	
				break;
			default:
				resultList.add(currentStatus);
				break;
			}
			break;
			
		case PENDING_VERIFICATION:
			switch (assigedGroup) {
			case DEVELOPMENT_TEAM:
				resultList.add(String.valueOf(StatusCode.VERIFIED));	
				break;
			default:
				resultList.add(currentStatus);
				break;
			}
			break;
			
		case VERIFIED:
			switch (assigedGroup) {
			case TESTING_TEAM:
				resultList.add(String.valueOf(StatusCode.COMPLETED));	
				break;
			default:
				resultList.add(currentStatus);
				break;
			}
			break;
			
		case CANCELED:
			switch (assigedGroup) {
			case DEVELOPMENT_TEAM:
				resultList.add(String.valueOf(StatusCode.CANCELED));
				break;
			}
			break;
			
		case COMPLETED:
			switch (assigedGroup) {
			case TESTING_TEAM:
				resultList.add(String.valueOf(StatusCode.COMPLETED));
				break;
			}
			break;
		}
		System.out.println("resultList Allowed List is -- " + resultList.size());
		return resultList;
	}

}