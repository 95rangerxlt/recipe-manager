package com.internal.recipes.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@RequestMapping(value = "/rmLogin", method = RequestMethod.GET)
	public String getLoginPage(@RequestParam(value="error", required=false) boolean error, ModelMap model) {
		logger.info("LoginController::request to show login page, error:" + error);
		
		if (error == true)
			model.put("error", "Invalid Credentials");
		else
			model.put("error", "");
				
		return "rmLogin";
	}


}
