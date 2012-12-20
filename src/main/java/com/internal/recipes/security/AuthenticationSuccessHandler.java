package com.internal.recipes.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.internal.recipes.domain.EventLog;
import com.internal.recipes.domain.EventType;
import com.internal.recipes.domain.User;
import com.internal.recipes.service.EventLogService;
import com.internal.recipes.service.UserService;

public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private EventLogService eventLogService;

	@Autowired
	private UserService userService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException  {
		User thisUser = userService.findByUserName(authentication.getName());
		EventLog el = new EventLog(thisUser.getFirstName() + " " + thisUser.getLastName(), EventType.EVENT_SECURITY, "logged in as " + thisUser.getUserName());
		eventLogService.create(el);		
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
