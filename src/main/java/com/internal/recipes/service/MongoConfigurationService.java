package com.internal.recipes.service;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;

import com.internal.recipes.domain.EventLog;
import com.internal.recipes.domain.Role;
import com.internal.recipes.domain.User;

@Component
public class MongoConfigurationService implements DbConfigurationService {

	@Autowired
	MongoOperations mongoOperations;
	
	@Autowired
	UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(MongoConfigurationService.class);
	
	@PostConstruct
	public void init() {
		
		logger.info("Verifying mongodb configuration.");
		
		if (! mongoOperations.collectionExists(EventLog.class)) {
			CollectionOptions options = new CollectionOptions(100000, 200, true);
			mongoOperations.createCollection("eventLog", options);
			logger.info("Configured eventLog as a capped collection");
		}
		
		if (! mongoOperations.collectionExists(User.class)) {
			// create the admin user
			User user = new User();
			user.setUserName("admin");
			String encrypted = new StandardPasswordEncoder().encode("Ravens52!"); 
			user.setPassword(encrypted);
			Set<Role> roles = new HashSet<Role>();
			roles.add(Role.ROLE_ADMINISTRATOR);
			user.setRoles(roles);
			userService.createUser(user);			
			logger.info("Configured user collection with default admin user");
		}
		
	}

}
