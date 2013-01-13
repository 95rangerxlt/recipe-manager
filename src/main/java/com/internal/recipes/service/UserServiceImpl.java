package com.internal.recipes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.internal.recipes.domain.EventLog;
import com.internal.recipes.domain.EventType;
import com.internal.recipes.domain.RecipeManagerEvent;
import com.internal.recipes.domain.User;
import com.internal.recipes.exception.UserNameNotUniqueException;
import com.internal.recipes.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	public User createUser(User user) {
		if (userRepository.findByUserName(user.getUserName()) != null) {
			throw new UserNameNotUniqueException(user.getUserName());
		}
		
		User newUser =  userRepository.save(user);
		String logData = "created user " + newUser.getUserName() + " " + newUser.getFirstName() + " " + newUser.getLastName() + " " + newUser.getEmailAddress();
		EventLog el = new EventLog(EventType.EVENT_USER_CREATED, logData);
		el.setActor(newUser.getUserName());
		publisher.publishEvent(new RecipeManagerEvent(newUser, el));
		return newUser;
	}


	public User findByUserName(String userName) {
		User user = userRepository.findByUserName(userName);
		if (user == null) {
			throw new UsernameNotFoundException("user: " + userName + " does not exist");
		}
		return user;
	}
	
	public User findOne(String id) {
		User user = userRepository.findOne(id);
		if (user == null) {
			throw new UsernameNotFoundException ("user: with id " + id + " does not exist");
		}
		return user;
	}

	public User updateUser(User user) {
		User u = findByUserName(user.getUserName());
		if (u == null) {
			throw new UsernameNotFoundException("user: " + user.getUserName() + " does not exist");			
		}
		
		String logData = "modified user " + user.getUserName() + " " + user.getFirstName() + " " + user.getLastName() + " " + user.getEmailAddress();
		EventLog el = new EventLog(EventType.EVENT_USER_MODIFIED, logData);
		el.setActor(user.getUserName());
		publisher.publishEvent(new RecipeManagerEvent(user, el));
		
		return userRepository.save(user);
	}

	public Boolean deleteUser(User user) {
		User u = findByUserName(user.getUserName());
		if (u == null) {
			throw new UsernameNotFoundException("user: " + user.getUserName() + " does not exist");			
		}
		
		String logData = "deleted user " + u.getUserName() + " " + u.getFirstName() + " " + u.getLastName() + " " + u.getEmailAddress();
		EventLog el = new EventLog(EventType.EVENT_USER_DELETED, logData);
		el.setActor(u.getUserName());
		publisher.publishEvent(new RecipeManagerEvent(user, el));
		
		userRepository.delete(u);
		return true;
	}

	public List<User> getAllUsers() {
		return userRepository.getAll();
	}
	
	public User getUserInfo(String userName) {
		System.out.println("UserServiceImpl::getUserInfo for userName:" + userName);
		return userRepository.getUserInfo(userName);	
	}
}
