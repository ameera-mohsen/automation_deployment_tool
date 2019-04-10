package com.dxc.automation.service;

import java.util.List;
import java.util.Map;
import com.dxc.automation.entity.Users;

public interface UserServices {

	Users login(String email, String password);

	Users addUser(Users user);

	Users findUserById(String userId);

	Users updateUser(Users user);

	String deleteUser(String userId);

	List<Users> listUsers();

	Map<String, String> findUserDetails(String id);
}
