package com.internal.recipes.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.internal.recipes.domain.Role;
import com.internal.recipes.domain.User;
import com.internal.recipes.security.RecipeUserDetails;
import com.internal.recipes.service.UserService;

@Controller
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<User> getAllUsers(Principal p) {
		logger.info("User {}::Request to get all users.", p.getName());
		
		List<User> userList = userService.getAllUsers();
		for (User user : userList) {
			user.setPassword("");
		}		
		return userList;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody User create(@RequestBody final User entity, Principal p) {
		logger.info("User {}::Request to create a user", p.getName());

		String encoded = new StandardPasswordEncoder().encode(entity.getPassword()); 
		entity.setPassword(encoded);
		
		User u = userService.createUser(entity);
		u.setPassword("");
		return u;
	}

	@RequestMapping(method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody User update(@RequestBody final User entity, Principal p) {
		RecipeUserDetails ud = (RecipeUserDetails) ((Authentication)p).getPrincipal();
		User thisUser = ud.getUser();
		
		logger.info("User {}::Request to update a user", thisUser.getUserName());

		// don't blow away the stored password, don't return password to the client
		User u = userService.findByUserName(entity.getUserName());
		entity.setPassword(u.getPassword());
		u = userService.updateUser(entity);
		u.setPassword("");
		return u;
	}
	
	@RequestMapping(value = "/myAccount", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody User updateMyAccount(@RequestBody final User entity, Principal p) {
		RecipeUserDetails ud = (RecipeUserDetails) ((Authentication)p).getPrincipal();
		User thisUser = ud.getUser();
		
		logger.info("User {}::Request to update myAccount", thisUser.getUserName());

		String encrypted = new StandardPasswordEncoder().encode(entity.getPassword()); 
		entity.setPassword(encrypted);
		userService.updateUser(entity);
		entity.setPassword("");
		return entity;
	}
	
	@RequestMapping(value = "/{userName}", method = RequestMethod.GET)
	public @ResponseBody User getUser(@PathVariable("userName") final String userName, Principal p) {
		logger.info("User {}::Request to get a user with userName: {}", p.getName(), userName);
		User user = userService.findByUserName(userName);
		user.setPassword("");
		return user;
	}
	
	@RequestMapping(value = "/currentUser", method = RequestMethod.GET)
	public @ResponseBody User getCurrentUser(Principal p) {
		logger.info("User {}::Request to get a current user", p.getName());
		User user = userService.findByUserName(p.getName());
		user.setPassword("");
		return user;
	}

	
	@RequestMapping(value = "/{userName}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteUser(@PathVariable("userName") final String userName, Principal p) {
		RecipeUserDetails ud = (RecipeUserDetails) ((Authentication)p).getPrincipal();
		User thisUser = ud.getUser();
		logger.info("User {}::Request to delete a user", thisUser.getUserName());
		
		User u = userService.findByUserName(userName);	

		userService.deleteUser(u);
	}
	
	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Role[] getRoles(Principal p) {
		logger.info("User {}::Request to get defined roles", p.getName());
		return Role.values();
	}
	
	@ExceptionHandler({UsernameNotFoundException.class})
    ResponseEntity<String> handleNotFounds(Exception e) {
    	logger.error(e.getMessage());
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

}
