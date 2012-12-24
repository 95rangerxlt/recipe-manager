package com.internal.recipes.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.internal.recipes.domain.Recipe;
import com.internal.recipes.domain.User;
import com.internal.recipes.exception.RecipeDoesNotExistException;
import com.internal.recipes.security.RecipeUserDetails;
import com.internal.recipes.service.RecipeService;
import com.internal.recipes.service.UserService;

@Controller
@RequestMapping(value = "/recipes")
public class RecipeController {

	@Autowired
	private RecipeService recipeService;
	
	@Autowired 
	private UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	List<Recipe> getAllRecipes() {
		logger.info("Request to get all recipes.");

		List<Recipe> recipes = recipeService.getAllRecipes();
		for (Recipe recipe : recipes) {
			if (recipe.getContributer() != null)
				recipe.getContributer().setPassword("");
		}
		return recipes;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	Recipe getRecipe(@PathVariable("id") final String id) throws RecipeDoesNotExistException {
		logger.info("Request to get one recipe with id: {}", id);

		Recipe recipe = recipeService.get(id);
		if (recipe.getContributer() != null)
			recipe.getContributer().setPassword("");
		return recipe;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody
	Recipe create(@RequestBody final Recipe entity, Principal p) {
		RecipeUserDetails ud = (RecipeUserDetails) ((Authentication) p).getPrincipal();
		User thisUser = ud.getUser();
		entity.setContributer(thisUser);

		logger.info("Request to create a recipe");

		Recipe recipe = recipeService.create(entity);
		if (recipe.getContributer() != null)
			recipe.getContributer().setPassword("");

		return recipe;
	}

	@RequestMapping(method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody
	Recipe update(@RequestBody final Recipe entity, Principal p) throws RecipeDoesNotExistException {
		RecipeUserDetails ud = (RecipeUserDetails) ((Authentication) p).getPrincipal();
		User thisUser = ud.getUser();

		logger.info("Request to update a recipe");

		User uc;
		if (entity.getContributer().getId() == "")
			uc  = thisUser;
		else
			uc = userService.findOne(entity.getContributer().getId());
		
		entity.setContributer(uc);

		Recipe recipe = recipeService.update(entity);
		if (recipe.getContributer() != null)
			recipe.getContributer().setPassword("");
		return recipe;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody
	Recipe deleteRecipe(@PathVariable("id") final String id, Principal p) throws RecipeDoesNotExistException {
		logger.info("Request to delete a recipe");
		Recipe recipe = recipeService.get(id);
		recipeService.delete(recipe);

		if (recipe.getContributer() != null)
			recipe.getContributer().setPassword("");
		return recipe;
	}

	@ExceptionHandler({ RecipeDoesNotExistException.class })
	ResponseEntity<String> handleNotFounds(Exception e) {
		logger.error(e.getMessage());
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

}
