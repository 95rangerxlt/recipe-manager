package com.internal.recipes.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;

import com.internal.recipes.domain.EventLog;
import com.internal.recipes.service.EventLogService;

public class AuthenticationFailureHandler extends ExceptionMappingAuthenticationFailureHandler {
	@Autowired
	private EventLogService eventLogService;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		EventLog el = new EventLog("Administrator - auto generated", "Login Failure, reason: " + exception.getMessage());
		eventLogService.create(el);		
		super.onAuthenticationFailure(request, response, exception);					
	}
}
