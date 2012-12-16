package com.internal.recipes;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.internal.recipes.domain.Recipe;
import com.internal.recipes.service.RecipeDoesNotExistException;
import com.internal.recipes.service.RecipeService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Autowired
	private RecipeService recipeService;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! the client locale is "+ locale.toString());
		
		model.addAttribute("recipes", recipeService.getAllRecipes());		
		return "home";
	}
	
	@RequestMapping(value = "userManager", method = RequestMethod.GET)
	public String userManager(Locale locale, Model model) {
		logger.info("Request for the userManager");		
		return "userManager";
	}

	@RequestMapping(value = "recipeManager", method = RequestMethod.GET)
	public String recipeManager(Locale locale, Model model) {
		logger.info("Request for the recipeManager");		
		return "home";
	}
	
}
