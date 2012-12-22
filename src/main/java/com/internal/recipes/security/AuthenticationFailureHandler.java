package com.internal.recipes.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;

import com.internal.recipes.domain.EventLog;
import com.internal.recipes.domain.EventType;
import com.internal.recipes.domain.RecipeManagerEvent;

public class AuthenticationFailureHandler extends ExceptionMappingAuthenticationFailureHandler {
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		EventLog el = new EventLog(EventType.EVENT_SECURITY, "Login Failure, reason: " + exception.getMessage());
		el.setActor("Administrator - auto generated");
		publisher.publishEvent(new RecipeManagerEvent(this, el));
		this.setDefaultFailureUrl("/spring_security_login?login_error");
		super.onAuthenticationFailure(request, response, exception);					
	}
}
