package com.dxc.automation.common;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.dxc.automation.entity.Email;

public class APICaller {

	//static String EMAIL_URL = "http://localhost:8080/api/Notification";
	static String EMAIL_URL = "https://arcane-fjord-15809.herokuapp.com/api/Notification";

	public static ResponseEntity<Email> EmailAPI(String to) {
		RestTemplate temp = new RestTemplate();
		ResponseEntity<Email> response = null;
		HttpEntity<Email> request = null;
		try {
			request = new HttpEntity<>(buildEmail(to), buildHeader());
			response = temp.exchange(EMAIL_URL, HttpMethod.POST, request, Email.class);
		} catch (Exception e) {
			System.err.println(e);
		}
		return response;
	}


	
	private static Email buildEmail(String to) {
		System.err.println("build email");
		Email email = new Email();
		email.setSubject("Automation Service - User Created Confirmation");
		email.setTo(to);
		email.setCc("karim.say.ahmed@gmail.com");
		email.setBody("User Created Successfully for the following email: " + to);
		return email;
	}

	private static HttpHeaders buildHeader() {
		HttpHeaders headers = new HttpHeaders();
		String plainCreds = "sadad123:sadad123";
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);
		headers.add("Authorization", "Basic " + base64Creds);
		headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0");
		headers.add("Accept", "text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*;q=0.8");
		headers.add("Accept-Language", "en-US,en;q=0.5");
		headers.add("Accept-Encoding", "gzip, deflate");
		headers.add("Connection", "keep-alive");
		return headers;
	}
}
