package com.dxc.automation.service;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.dxc.automation.entity.Users;

public interface LoginServices extends MongoRepository<Users, String> {

	@Query(value = "{ 'email' : ?0, 'credentials.password' : ?1 }")
	public Users login(String email, String password);

}
