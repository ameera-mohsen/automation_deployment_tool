package com.sadad.automation.deploymentrequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import com.sadad.automation.deploymentrequest.controller.DeploymentRequestController;
import com.sadad.automation.deploymentrequest.service.DeploymentRequestService;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@ComponentScan(basePackageClasses = {DeploymentRequestController.class,DeploymentRequestService.class})
public class DeploymentRequestServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeploymentRequestServiceApplication.class, args);
	}

}

