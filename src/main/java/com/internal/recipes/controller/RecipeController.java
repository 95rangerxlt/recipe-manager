package com.internal.recipes.controller;

import java.io.File;
import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.internal.recipes.domain.CloudFilesObject;
import com.internal.recipes.domain.Recipe;
import com.internal.recipes.domain.User;
import com.internal.recipes.exception.RecipeDoesNotExistException;
import com.internal.recipes.security.RecipeUserDetails;
import com.internal.recipes.service.CloudFilesService;
import com.internal.recipes.service.RecipeService;
import com.internal.recipes.service.UserService;

@Controller
@RequestMapping(value = "/recipes")
public class RecipeController {

	@Autowired
	private RecipeService recipeService;
	
	@Autowired 
	private UserService userService;
	
	@Autowired
	private CloudFilesService cloudFilesService;

	private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	List<Recipe> getAllRecipes() {
		logger.info("Request to get all recipes.");

		List<Recipe> recipes = recipeService.getAllRecipes();
		return recipes;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	Recipe getRecipe(@PathVariable("id") final String id) throws RecipeDoesNotExistException {
		logger.info("Request to get one recipe with id: {}", id);

		Recipe recipe = recipeService.get(id);
		return recipe;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody
	Recipe create(@RequestBody final Recipe entity, Principal p) {
		RecipeUserDetails ud = (RecipeUserDetails) ((Authentication) p).getPrincipal();
		User thisUser = ud.getUser();
		entity.setContributerUserName(thisUser.getUserName());

		logger.info("Request to create a recipe");

		Recipe recipe = recipeService.create(entity);
		return recipe;
	}

	@RequestMapping(method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody
	Recipe update(@RequestBody final Recipe entity, Principal p) throws RecipeDoesNotExistException {
		logger.info("Request to update a recipe");
		
		// preserve the contributer info
		Recipe r = recipeService.get(entity.getRecipeId());
		entity.setContributerUserName(r.getContributerUserName());
		Recipe recipe = recipeService.update(entity);
		return recipe;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody
	Recipe deleteRecipe(@PathVariable("id") final String id, Principal p) throws RecipeDoesNotExistException {
		logger.info("Request to delete a recipe");
		Recipe recipe = recipeService.get(id);
		recipeService.delete(recipe);
		return recipe;
	}
	
	@RequestMapping(value="/uploadFile", method=RequestMethod.POST)
	public @ResponseBody String uploadFile(MultipartFile file, String basename, String target)  {
		
		logger.info("Request to upload a file, name is {}, target is {}", basename, target);
		cloudFilesService.storeObject(file, target, basename);
		
		try {
			File f = new File("/tmp/herbcooking/" + basename);
			file.transferTo(f);
		} catch (IllegalStateException e) {
			logger.info("exception:{}", e.getMessage());
		} catch (IOException e) {
			logger.info("exception:{}", e.getMessage());
		}
	 
		return basename;
	}
	
	@RequestMapping(value="/recipePics/{id}", method = RequestMethod.GET)
	public @ResponseBody
	List<CloudFilesObject> getAllRecipePics(@PathVariable("id") String recipeId) {
		logger.info("Request to get all recipe pics for id: {}", recipeId);
		return cloudFilesService.getObjects(recipeId + "/pics");
	}


	@ExceptionHandler({ RecipeDoesNotExistException.class })
	ResponseEntity<String> handleNotFounds(Exception e) {
		logger.error(e.getMessage());
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

}
