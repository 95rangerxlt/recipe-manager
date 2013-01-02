package com.internal.recipes.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.internal.recipes.domain.EmailMessage;
import com.internal.recipes.domain.EmailMessageResponseStatus;
import com.internal.recipes.domain.User;
import com.internal.recipes.service.EmailService;
import com.internal.recipes.service.UserService;

@Controller
@RequestMapping(value = "/email")
public class EmailController {
	
	@Autowired
	EmailService emailService;
	
	@Autowired 
	private UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(EmailController.class);
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody EmailMessageResponseStatus send(@RequestBody final EmailMessage message, Principal p) {
		User fromUser = userService.getUserInfo(p.getName());
		message.setSenderName(fromUser.getFirstName() + " " + fromUser.getLastName());
		message.setSenderEmail(fromUser.getEmailAddress());

		User toUser = userService.getUserInfo(message.getReceiverUsername());
		message.setReceiverEmail(toUser.getEmailAddress());
		message.setReceiverName(toUser.getFirstName() + " " + toUser.getLastName());
		
		logger.info("Request to send an email");
		
		return emailService.send(message);
	}	

}
