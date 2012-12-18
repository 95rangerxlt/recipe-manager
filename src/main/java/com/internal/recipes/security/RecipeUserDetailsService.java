package com.internal.recipes.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.internal.recipes.repository.UserRepository;

@Component
public class RecipeUserDetailsService implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;

	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		if (userName == null || userName.equals(""))
			throw new UsernameNotFoundException("username field not supplied");
		return new RecipeUserDetails(userRepository.findByUserName(userName));
	}

}
