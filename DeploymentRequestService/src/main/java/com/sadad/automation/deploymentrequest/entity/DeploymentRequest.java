package com.sadad.automation.deploymentrequest.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = "DeploymentRequest")
public class DeploymentRequest {

	@Id
	private String id;
	@Field("Status")
	private String status;
	@Field("Environment")
	private String environment;
	@Field("DefectID")
	private String defectId;
	@Field("assignOnGroup")
	private String assignOnGroup;
	@Field("RequestDate")
	@DateTimeFormat(pattern="yyyy-MM-dd'T'hh:mm:ss")
	private Date requestDate;
	@Field("DeploymentTime")
	@DateTimeFormat(pattern="yyyy-MM-dd'T'hh:mm:ss")
	private Date deploymentTime;
	@Field("Reason")
	private String reason;
	@Field("ReleaseNote")
	private String releaseNote;
	@Field("Layer")
	private List<String> layer;
	@Field("AffectedService")
	private List<String> affectedService;
	private List<RequestInfo> requestInfo;
	@Field("initiatorUser")
	private Users initiatorUser;
	@Field("assignOnUser")
	private Users assignOnUser;
	@Field("pickedByUser")
	private Users pickedByUser;
	@Field("RequestSubject")
	private String requestSubject;

	public Users getPickedByUser() {
		return pickedByUser;
	}

	public void setPickedByUser(Users pickedByUser) {
		this.pickedByUser = pickedByUser;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getDefectId() {
		return defectId;
	}

	public void setDefectId(String defectId) {
		this.defectId = defectId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReleaseNote() {
		return releaseNote;
	}

	public void setReleaseNote(String releaseNote) {
		this.releaseNote = releaseNote;
	}

	public List<String> getLayer() {
		return layer;
	}

	public void setLayer(List<String> layer) {
		this.layer = layer;
	}

	public List<String> getAffectedService() {
		return affectedService;
	}

	public void setAffectedService(List<String> affectedService) {
		this.affectedService = affectedService;
	}

	public List<RequestInfo> getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(List<RequestInfo> requestInfo) {
		this.requestInfo = requestInfo;
	}

	public Users getInitiatorUser() {
		return initiatorUser;
	}

	public void setInitiatorUser(Users initiatorUser) {
		this.initiatorUser = initiatorUser;
	}

	public Users getAssignOnUser() {
		return assignOnUser;
	}

	public void setAssignOnUser(Users assignOnUser) {
		this.assignOnUser = assignOnUser;
	}

	public String getAssignOnGroup() {
		return assignOnGroup;
	}

	public void setAssignOnGroup(String assignOnGroup) {
		this.assignOnGroup = assignOnGroup;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public Date getDeploymentTime() {
		return deploymentTime;
	}

	public void setDeploymentTime(Date deploymentTime) {
		this.deploymentTime = deploymentTime;
	}
	
	public String getRequestSubject() {
		return requestSubject;
	}

	public void setRequestSubject(String requestSubject) {
		this.requestSubject = requestSubject;
	}
	
}
