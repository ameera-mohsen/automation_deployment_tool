package com.dxc.automation.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.dxc.automation.entity.AchnowledgeResponse;
import com.dxc.automation.entity.Email;
import com.dxc.automation.service.EmailService;

@RestController
@RequestMapping("/api")
public class MailController {
	
	@Autowired
	private EmailService emailService;
	private static final Logger LOGGER = LoggerFactory.getLogger(MailController.class);


	/**
	 * Operation just for get the JSON structure used in sending simple emails run
	 * the project and use the following url : http://localhost:8080/api/mailform
	 * 
	 * @return
	 */
	@GetMapping("/mailform")
	public Email getEmailForm() {
		LOGGER.debug("0--getEmailForm----------------- ");
		System.err.println("get mail");
		Email email = new Email();
		email.setTo("--Replace with Email--");
		email.setCc("--Replace with Email--");
		email.setBody("--Replace with Body--");
		email.setSubject("Automation Service Testing - Email Service");
		return email;
	}

	/**
	 * Operation is for sending email run the project and use the following URL :
	 * http://localhost:8080/api/Notification
	 */
	@PostMapping("/Notification")
	public void sendEmail(@RequestBody Email email) {
		LOGGER.debug("1----sendEmail--------------- ");
		System.err.println("send Email");
		emailService.sendEmail(email.getTo(), email.getCc(), email.getSubject(), email.getBody());
	}

	/**
	 * Operation is for sending email run the project and use the following URL :
	 * http://localhost:8080/api/AcknowledgeNotification
	 */
	@PostMapping("/AcknowledgeNotification")
	public AchnowledgeResponse sendEmailAcknowledge(@RequestBody Email email) {
		LOGGER.debug("0------------------- ");

		AchnowledgeResponse ach;
		if (emailService.sendEmailAcknowledge(email.getTo(), email.getCc(), email.getSubject(), email.getBody())) {
			ach = new AchnowledgeResponse(200, "Success", "Email sent successfully");
		} else {
			ach = new AchnowledgeResponse(500, "Failed", "Email failed");
		}
		return ach;
	}
	
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void handleTodoNotFound(Exception e) {
		LOGGER.debug("***Handling error with message: {}", e.getMessage() + " on " + new Date());

	}
}
