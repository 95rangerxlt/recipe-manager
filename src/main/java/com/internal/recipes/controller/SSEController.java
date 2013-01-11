package com.internal.recipes.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.internal.recipes.event.RecipeEventListener;


@Controller
public class SSEController {
	
	@Autowired
	private RecipeEventListener recipeEventListener;

	private static final Logger logger = LoggerFactory.getLogger(SSEController.class);
	private int count = 0;
	
	@RequestMapping(value = "/sseTester", method = RequestMethod.GET)
	public String sseTester() {
		logger.info("SSE Tester invoked");
		return "sseTester";
	}

	@RequestMapping("/sse")
	public @ResponseBody String sendEvent(HttpServletResponse response) {
		this.count++;
		
		response.setContentType("text/event-stream");
		
		try {
			Thread.sleep(60000);
		}
		catch (InterruptedException e) {
			logger.error("SSE: InterruptedException, msg is " + e.getMessage());
		}

		String event = "data: Testing Sequence Number " + count + "\n\n"; 
		logger.info("SSE: sending an Event: " + event);
		return event;
	}

}
