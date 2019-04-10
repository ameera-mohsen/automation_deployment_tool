package com.dxc.automation.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.dxc.automation.common.CustomResponse;
import com.dxc.automation.common.Status;
import com.dxc.automation.entity.Credentials;
import com.dxc.automation.entity.Users;
import com.dxc.automation.service.UserServices;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserServices userRepo;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@GetMapping("/AllUsers")
	public ResponseEntity<CustomResponse> listUsers() {
		LOGGER.info("Entering listUsers :");
		List<Users> user = userRepo.listUsers();
		return new ResponseEntity<CustomResponse>(new CustomResponse(new Status(200, "Success"), user), HttpStatus.OK);
	}

	@GetMapping("/FindId/{id}")
	public ResponseEntity<CustomResponse> findUserId(@PathVariable String id) {
		LOGGER.info("Entering findUserId : " + id);
		Users user = userRepo.findUserById(id);
		return new ResponseEntity<CustomResponse>(new CustomResponse(new Status(200, "Success"), user), HttpStatus.OK);
	}

	@GetMapping("/FindNameandEmail/{id}")
	public Map<String, String> findUserDetails(@PathVariable String id) {
		LOGGER.info("Entering findUserId : " + id);
		return userRepo.findUserDetails(id);
	}
	
	@PutMapping("/UpdateUser")
	public ResponseEntity<CustomResponse> updateUser(@RequestBody Users user) {
		LOGGER.info("Entering updateUser :");
		Users updatedUser = userRepo.updateUser(user);
		return new ResponseEntity<CustomResponse>(new CustomResponse(new Status(200, "Success"), updatedUser), HttpStatus.OK);
	}
	

	@PostMapping("/NewUser")
	public ResponseEntity<CustomResponse> createUser(@RequestBody Users u) {
		LOGGER.info("Entering newUser :::");
		Users user = userRepo.addUser(u);
		return new ResponseEntity<CustomResponse>(new CustomResponse(new Status(200, "Success"), user), HttpStatus.OK);
	}

	@GetMapping("/login")
	public ResponseEntity<CustomResponse> login(@RequestBody Credentials credentials) {
		Users user = userRepo.login(credentials.getEmail(), credentials.getUserPassword());
		return new ResponseEntity<CustomResponse>(new CustomResponse(new Status(200, "Success"), user), HttpStatus.OK);
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<CustomResponse> handleTodoNotFound(String emailOrId) {
		LOGGER.debug("***Handling error with message: {} " + new Date());
		return new ResponseEntity<CustomResponse>(
				new CustomResponse(new Status(500, "Fail, User " + emailOrId + " not found")), HttpStatus.BAD_REQUEST);
	}
}
