package com.sadad.automation.deploymentrequest.common;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.sadad.automation.deploymentrequest.entity.Email;
import com.sadad.automation.deploymentrequest.entity.Users;

public class APICaller {

	static String EMAIL_URL = "https://arcane-fjord-15809.herokuapp.com/api/Notification";
	static String USER_URL = "http://localhost:8066/api/FindNameandEmail/FindNameandEmail";

	
	
	public static Users UserAPI(){
		System.err.println("USER API");
		// SARA Omran ID 
		//String userId = "5c49c60e1b8f732ffcff44de";
		Users userDTO = new Users("5c49c60e1b8f732ffcff44de", "Sara Omran", "Sara.Om@dxc.com", "TESTING");
/*
		RestTemplate temp = new RestTemplate();
		HttpEntity<String> request = null;
		ResponseEntity<Users> response = null;
		try {
			// Enrich request with required request body and Header
			request = new HttpEntity<String>(userId, buildHeader());
			// Send request after define the requestBody, httpMethod, API endPoind, and
			System.out.println("request :: " + request.getBody());
			// requestType
			// NOTE: HTTPMethod change based on outgoing request, PUT,POST,GET or DELETE
			response = temp.exchange(USER_URL, HttpMethod.GET, request, Users.class);
			System.err.println(" Name ::: " + response.getBody().getDisplayName());
		} catch (Exception e) {
			System.err.println("exc" + e);
			throw new DeploymentReqException("User " + userId + " Not exist in the system"); 
		}*/
		return userDTO;
	}
	
	
	public static ResponseEntity<Email> EmailAPI(String to, String body) {
		// RestTemplate used to perform HTTP requests.
		RestTemplate temp = new RestTemplate();
		ResponseEntity<Email> response = null;
		HttpEntity<Email> request = null;
		try {
			// Enrich request with required request body and Header
			request = new HttpEntity<>(buildEmail(to, body), buildHeader());
			// Send request after define the requestBody, httpMethod, API endPoind, and
			// requestType
			// NOTE: HTTPMethod change based on outgoing request, PUT,POST,GET or DELETE
			response = temp.exchange(EMAIL_URL, HttpMethod.POST, request, Email.class);
		} catch (Exception e) {
			System.err.println(e);
		}
		return response;
	}

	/*
	 * Below is a common method used to build the email before send it as a POST
	 * Request
	 */
	private static Email buildEmail(String to, String body) {
		System.err.println("build email");
		Email email = new Email();
		email.setSubject("Automation Service - Task assigned on you");
		email.setTo(to);
//		email.setCc("karim.say.ahmed@gmail.com");
		email.setCc("doaaessam4455@gmail.com");
		email.setBody(body);
		return email;
	}

	/*
	 * Below is a common method for building Header for any request. NOTE:
	 * Authentication * plainCreds Object * may change if we change the API
	 * credential.
	 */
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
