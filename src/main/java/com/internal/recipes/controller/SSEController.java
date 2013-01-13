package com.internal.recipes.controller;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		
	@RequestMapping(value = "/sseTester", method = RequestMethod.GET)
	public String sseTester() {
		logger.info("/sseTester invoked");
		return "sseTester";
	}

	@RequestMapping(value = "/sse", method = RequestMethod.GET)
	public @ResponseBody void sendEvent(HttpSession session, HttpServletResponse response, Principal p) throws IOException {
		//logger.info("/sse invoked with request id: " + response.getOutputStream().hashCode());
		response.setContentType("text/event-stream");
		eventListener.subscribe(Integer.toBinaryString(response.getOutputStream().hashCode()), response.getOutputStream());
	}
}