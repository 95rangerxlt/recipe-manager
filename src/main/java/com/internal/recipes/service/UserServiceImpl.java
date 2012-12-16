package com.internal.recipes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.internal.recipes.domain.User;
import com.internal.recipes.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	public User createUser(User user) {
		if (userRepository.findByUserName(user.getUserName()) != null) {
			throw new UserNameNotUniqueException(user.getUserName());
		}
		return userRepository.save(user);
	}

	public User findByUserName(String userName) {
		User user = userRepository.findByUserName(userName);
		if (user == null) {
			throw new UsernameNotFoundException("user: " + userName + "does not exist");
		}
		return user;
	}

	public User updateUser(User user) {
		if (!userRepository.exists(user.getId())) {
			throw new UsernameNotFoundException("user: " + user.getUserName() + "does not exist");
		}
		return userRepository.save(user);
	}

	public Boolean deleteUser(User user) {
		if (!userRepository.exists(user.getId())) {
			return false;
		}
		userRepository.delete(user);
		return true;
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

}
