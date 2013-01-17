package com.internal.recipes.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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

import com.internal.recipes.domain.EmailMessage;
import com.internal.recipes.domain.EmailMessageResponseStatus;
import com.internal.recipes.domain.EventLog;
import com.internal.recipes.domain.EventType;
import com.internal.recipes.domain.RecipeManagerEvent;
import com.internal.recipes.domain.Role;
import com.internal.recipes.domain.User;
import com.internal.recipes.exception.UserEmailAddressNotFoundException;
import com.internal.recipes.security.RecipeUserDetails;
import com.internal.recipes.service.EmailService;
import com.internal.recipes.service.UserService;

@Controller
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	private ApplicationEventPublisher publisher;


	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<User> getAllUsers(Principal p) {
		logger.info("User {}::Request to get all users.", p.getName());
		
		List<User> userList = userService.getAllUsers();
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
		logger.info("User {}::Request to get current user", p.getName());
		User user = userService.findByUserName(p.getName());
		user.setPassword("");
		return user;
	}

	@RequestMapping(value = "/userInfo/{userName}", method = RequestMethod.GET)
	public @ResponseBody User getUserInfo(@PathVariable("userName") final String userName, Principal p) {
		logger.info("User {}::Request to get user info for user {}", p.getName(), userName);
		User user = userService.getUserInfo(userName);
		return user;
	}
	
	// be careful when calling this, you must add a slash char after the email address, otherwise
	// spring will truncated the email, thus removing the .com portion of it.
	@RequestMapping(value = "/getUserByEmailAddress/{userEmail}", method = RequestMethod.GET)
	public @ResponseBody User getUserWithEmailAddress(@PathVariable("userEmail") final String userEmail, Principal p) {
		logger.info("getUserByEmailAddress::Request to get user for user with email address {}", userEmail);
		User user = userService.findByEmailAddress(userEmail);
		return user;
	}
	
	@RequestMapping(value = "/recoverAccountPassword", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody EmailMessageResponseStatus recoverAccountPassword(@RequestBody final User entity, Principal p) {

		String tempPassword = entity.getFirstName() + UUID.randomUUID().toString().substring(0,7);
		String encrypted = new StandardPasswordEncoder().encode(tempPassword); 
		
		User user = userService.findByEmailAddress(entity.getEmailAddress());
		user.setPassword(encrypted);
		userService.updateUser(user);
		
		// now send email to user
		EmailMessage message = new EmailMessage();
		message.setSenderName("herbcooking.net");
		message.setSenderEmail("herbcooking.net");
		message.setReceiverEmail(entity.getEmailAddress());
		message.setReceiverName(entity.getFirstName() + " " + entity.getLastName());
		message.setSubject("Account Recovery Info from herbcooking.net");
		message.setBody("Your password for herbcooking.net has been reset to <b>" + tempPassword + "</b>");

		logger.info("recoverAccountPassword::Request to recover account for account with username ", entity.getUserName());
		logger.info("recoverAccountPassword::Temporary password set to {}", tempPassword);
		logger.info("recoverAccountPassword::Encrypted password set to {}", encrypted);
		
		EmailMessageResponseStatus emrs = emailService.send(message);
		emrs.setMessage("Your password has been reset, please check your <b>" + entity.getEmailAddress() + "</b> email for details");

		EventLog el = new EventLog(EventType.EVENT_SECURITY,  "password reset for user " + user.getUserName());
		el.setActor(user.getUserName());
		publisher.publishEvent(new RecipeManagerEvent(this, el));

		return emrs;
	}
	
	@RequestMapping(value = "/recoverAccountUsername", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody EmailMessageResponseStatus recoverAccountUsername(@RequestBody final User entity, Principal p) {

		User user = userService.findByEmailAddress(entity.getEmailAddress());
		
		// now send email to user
		EmailMessage message = new EmailMessage();
		message.setSenderName("herbcooking.net");
		message.setSenderEmail("herbcooking.net");
		message.setReceiverEmail(user.getEmailAddress());
		message.setReceiverName(user.getFirstName() + " " + user.getLastName());
		message.setSubject("Account Recovery Info from herbcooking.net");
		message.setBody("Your username for herbcooking.net is <b>" + user.getUserName() + "</b>");

		logger.info("recoverAccountPassword::Request to recover account for account username with username ", entity.getUserName());
		
		EmailMessageResponseStatus emrs = emailService.send(message);
		emrs.setMessage("Your account username info has been retrieved.  Please check your <b>" + entity.getEmailAddress() + "</b> email for details");

		EventLog el = new EventLog(EventType.EVENT_SECURITY,  "Username recovery request for user " + user.getUserName());
		el.setActor(user.getUserName());
		publisher.publishEvent(new RecipeManagerEvent(this, el));

		return emrs;
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

	@ExceptionHandler({UserEmailAddressNotFoundException.class})
    ResponseEntity<String> handleUserEmailAddressNotFoundException(Exception e) {
    	logger.error(e.getMessage());
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

}
