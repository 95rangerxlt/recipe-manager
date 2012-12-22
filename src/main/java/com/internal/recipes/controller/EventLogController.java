package com.internal.recipes.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.internal.recipes.domain.EventLog;
import com.internal.recipes.service.EventLogService;

@Controller
@RequestMapping(value = "/eventLogs")
public class EventLogController {
	@Autowired
	private EventLogService eventLogService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<EventLog> getAllEventLogs(Principal p) {
		logger.info("User {}::Request to get all eventLogs.", p.getName());
		return eventLogService.getAllRecentEventLogs();
	}

}
