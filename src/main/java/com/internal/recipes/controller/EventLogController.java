package com.internal.recipes.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.internal.recipes.domain.EventLog;
import com.internal.recipes.repository.EventLogRepository;

@Controller
@RequestMapping(value = "/eventLogs")
public class EventLogController {
	@Autowired
	private EventLogRepository eventLogRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<EventLog> getAllEventLogs(Principal principal) {
		logger.info("User {}::Request to get all eventLogs.", principal.getName());
		
		Pageable pageable = new PageRequest(0,100, new Sort(Direction.DESC, "logDate"));
		return eventLogRepository.findAll(pageable).getContent();
	}

}
