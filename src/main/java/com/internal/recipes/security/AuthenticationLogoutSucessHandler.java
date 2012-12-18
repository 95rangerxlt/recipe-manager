package com.internal.recipes.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.internal.recipes.domain.EventLog;
import com.internal.recipes.domain.User;
import com.internal.recipes.service.EventLogService;
import com.internal.recipes.service.UserService;

public class AuthenticationLogoutSucessHandler extends AbstractAuthenticationTargetUrlRequestHandler implements LogoutSuccessHandler {
	@Autowired
	private EventLogService eventLogService;

	@Autowired
	private UserService userService;

	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		User thisUser = userService.findByUserName(authentication.getName());
		EventLog el = new EventLog(thisUser.getFirstName() + " " + thisUser.getLastName(), "logged out as " + thisUser.getUserName());
		eventLogService.create(el);		
		super.handle(request, response, authentication);
	}
}