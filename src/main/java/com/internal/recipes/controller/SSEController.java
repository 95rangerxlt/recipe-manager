package com.internal.recipes.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import com.internal.recipes.event.SSERecipeEventListener;



@Controller
public class SSEController {
	
	@Autowired
	SSERecipeEventListener eventListener;
	
	private static final Logger logger = LoggerFactory.getLogger(SSEController.class);
	private int count = 0;
	
	@RequestMapping(value = "/sseTester", method = RequestMethod.GET)
	public String sseTester() {
		logger.info("SSE Tester invoked");
		return "sseTester";
	}

	@RequestMapping("/sse")
	public @ResponseBody void sendEvent(HttpServletResponse response) throws IOException {
		response.setContentType("text/event-stream");
		eventListener.setServletOutputStream(response.getOutputStream());
		eventListener.subscribe();				
	}
}