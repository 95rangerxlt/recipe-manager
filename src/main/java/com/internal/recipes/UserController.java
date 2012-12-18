package com.internal.recipes;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import com.internal.recipes.domain.EventLog;
import com.internal.recipes.security.RecipeUserDetails;
import com.internal.recipes.service.EventLogService;
import com.internal.recipes.service.UserService;

@Controller
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	EventLogService eventLogService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<User> getAllUsers(Principal p) {
		logger.info("User {}::Request to get all users.", p.getName());
		return userService.getAllUsers();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody User create(@RequestBody final User entity, Principal p) {
		RecipeUserDetails ud = (RecipeUserDetails) ((Authentication)p).getPrincipal();
		User thisUser = ud.getUser();
		
		logger.info("User {}::Request to create a user", thisUser.getUserName());

		String logData = "created user " + entity.getUserName() + " " + entity.getFirstName() + " " + entity.getLastName() + " " + entity.getEmailAddress();
		EventLog el = new EventLog(thisUser.getFirstName() + " " + thisUser.getLastName(), logData);
		eventLogService.create(el);
		
		return userService.createUser(entity);
	}

	@RequestMapping(method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody User update(@RequestBody final User entity, Principal p) {
		RecipeUserDetails ud = (RecipeUserDetails) ((Authentication)p).getPrincipal();
		User thisUser = ud.getUser();
		
		logger.info("User {}::Request to update a user", thisUser.getUserName());

		//User thisUser = userService.findByUserName(p.getName());
		String logData = "modified user " + entity.getUserName() + " " + entity.getFirstName() + " " + entity.getLastName() + " " + entity.getEmailAddress();
		EventLog el = new EventLog(thisUser.getFirstName() + " " + thisUser.getLastName(), logData);
		eventLogService.create(el);		
		
		return userService.updateUser(entity);
	}
	
	@RequestMapping(value = "/{userName}", method = RequestMethod.GET)
	public @ResponseBody User getUser(@PathVariable("userName") final String userName, Principal p) {
		logger.info("User {}::Request to get a user with userName: {}", p.getName(), userName);
		return userService.findByUserName(userName);
	}
	
	@RequestMapping(value = "/{userName}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteUser(@PathVariable("userName") final String userName, Principal p) {
		RecipeUserDetails ud = (RecipeUserDetails) ((Authentication)p).getPrincipal();
		User thisUser = ud.getUser();
		logger.info("User {}::Request to delete a user", thisUser.getUserName());
		
		User u = userService.findByUserName(userName);	

		String logData = "deleted user " + u.getUserName() + " " + u.getFirstName() + " " + u.getLastName() + " " + u.getEmailAddress();
		EventLog el = new EventLog(thisUser.getFirstName() + " " + thisUser.getLastName(), logData);
		eventLogService.create(el);		

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
