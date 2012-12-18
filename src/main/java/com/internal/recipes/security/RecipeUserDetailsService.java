package com.internal.recipes.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.internal.recipes.domain.EventLog;
import com.internal.recipes.domain.User;
import com.internal.recipes.repository.UserRepository;
import com.internal.recipes.service.EventLogService;

@Component
public class RecipeUserDetailsService implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	private EventLogService eventLogService;

	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userRepository.findByUserName(userName);
		if (user == null) {
			String msg = "No user with username '" + userName + "' found!";
			EventLog el = new EventLog("Administrator - auto generated", "Login Failure, reason: " + msg);
			eventLogService.create(el);		
            throw new UsernameNotFoundException(msg);
		}
		
		return new RecipeUserDetails(user);
	}
}
