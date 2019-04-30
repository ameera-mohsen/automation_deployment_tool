package com.dxc.automation.service;

public interface EmailService {

	boolean sendEmailAcknowledge(String to, String cc, String subject, String body);

	boolean sendEmailWithAttachmentAcknowledge(String to, String cc, String subject, String Bbody, String filePath);

	void sendEmail(String to, String cc, String subject, String body);

	void sendEmailWithAttachment(String to, String cc, String subject, String body, String filePath);
	
	void sendEmailToList(String[] toList, String cc, String subject, String body);

}
