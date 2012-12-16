package com.internal.recipes;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.internal.recipes.domain.Recipe;
import com.internal.recipes.domain.Role;
import com.internal.recipes.domain.User;
import com.internal.recipes.service.RecipeDoesNotExistException;
import com.internal.recipes.service.UserService;

@Controller
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<User> getAllUsers() {
		logger.info("Request to get all users.");
		return userService.getAllUsers();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody User create(@RequestBody final User entity) {
		logger.info("Request to create a user");
		return userService.createUser(entity);
	}

	@RequestMapping(method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody User update(@RequestBody final User entity) {
		logger.info("Request to update a user");
		return userService.updateUser(entity);
	}
	
	@RequestMapping(value = "/{userName}", method = RequestMethod.GET)
	public @ResponseBody User getUser(@PathVariable("userName") final String userName) {
		logger.info("Request to get a user with userName: {}", userName);
		return userService.findByUserName(userName);
	}
	
	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Role[] getRoles() {
		logger.info("Request to get defined roles");
		return Role.values();
	}
	
	@ExceptionHandler({UsernameNotFoundException.class})
    ResponseEntity<String> handleNotFounds(Exception e) {
    	logger.error(e.getMessage());
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

}
