package com.internal.recipes.service;

import java.util.List;

import com.internal.recipes.domain.User;

public interface UserService {
	
	User createUser(User user);
	
	User findByUserName(String userName);
	
	User updateUser(User user);
	
	Boolean deleteUser(User user);
	
	List<User> getAllUsers();
	
}
