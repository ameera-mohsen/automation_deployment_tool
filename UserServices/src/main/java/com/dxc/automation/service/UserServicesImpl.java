package com.dxc.automation.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.dxc.automation.common.APICaller;
import com.dxc.automation.entity.Users;

@Service
public class UserServicesImpl implements UserServices {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private LoginServices loginServices;

	@Override
	public Users login(String email, String password) {
		System.err.print("inside login");
		Users loggedinUser = loginServices.login(email, password);
		if (loggedinUser == null) {
			throw new UsernameNotFoundException(email + " not Found or bad credentials");
		} else {
			return loggedinUser;
		}
	}

	@Override
	public Users addUser(Users user) {
		System.err.println("inside register");
		System.err.println(user.getDisplayName());
		System.err.println(user.getEmail());
		Users u = mongoTemplate.insert(user);
		System.err.println("after insert");
		if (!u.get_id().equals(null) || u.get_id() != null) {
			System.err.print("inside if condition ");
			APICaller.EmailAPI(u.getEmail());
			return u;
		} else {
			throw new UsernameNotFoundException(" Error, Kindly try Again");
		}
	}

	@Override
	public Users updateUser(Users user) {
		Users updatedToUser = this.findUserById(user.get_id());
		Users updatedUser = mongoTemplate.save(updatedToUser);
		return updatedUser;
	}

	@Override
	public String deleteUser(String userId) {
		Users user = findUserById(userId);
		if (user == null) {
			throw new UsernameNotFoundException(userId + " not Found");
		} else {
			return userId;
		}
	}

	@Override
	public List<Users> listUsers() {
		return mongoTemplate.findAll(Users.class);
	}

	@Override
	public Users findUserById(String userId) {

		Users user = mongoTemplate.findById(userId, Users.class);
		if (user == null) {
			throw new UsernameNotFoundException(userId + " not Found");
		} else {
			return user;
		}
	}

	@Override
	public Map<String, String> findUserDetails(String id) {
		Map<String, String> map = new HashMap<>();
		Users user = this.findUserById(id);
		map.put("Name", user.getDisplayName());
		map.put("Email", user.getEmail());
		return map;
	}

}
