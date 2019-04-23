package com.dxc.automation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MailServiceApplication {

	public static void main(String[] args) {
		System.err.println("MailServiceApplication started....");
		SpringApplication.run(MailServiceApplication.class, args);
	}

}
