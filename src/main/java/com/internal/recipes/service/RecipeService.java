package com.internal.recipes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.internal.recipes.domain.EventLog;
import com.internal.recipes.domain.EventType;
import com.internal.recipes.domain.Recipe;
import com.internal.recipes.domain.RecipeManagerEvent;
import com.internal.recipes.repository.RecipeRepository;

@Service
public class RecipeService {
	
	@Autowired
	RecipeRepository recipeRepository;
	
	@Autowired
	ApplicationEventPublisher publisher;
	
	public List<Recipe> getAllRecipes() {
		return recipeRepository.findAll();
	}
	
	public Recipe create(Recipe recipe) {
		String logData = "created recipe " + recipe.getTitle();
		EventLog eventLog = new EventLog(EventType.EVENT_RECIPE_ADMINISTRATION, logData);
		publisher.publishEvent(new RecipeManagerEvent(this, eventLog));
		return recipeRepository.save(recipe);
	}
	
	public Boolean delete(Recipe recipe) {
		if (!recipeRepository.exists(recipe.getRecipeId())) {
			return false;
		}
		String logData = "deleted recipe " + recipe.getTitle();
		EventLog eventLog = new EventLog(EventType.EVENT_RECIPE_ADMINISTRATION, logData);
		publisher.publishEvent(new RecipeManagerEvent(this, eventLog));
		
		recipeRepository.delete(recipe);
		return true;
	}
	
	public Recipe update(Recipe recipe) {
		if (!recipeRepository.exists(recipe.getRecipeId())) {
			throw new RecipeDoesNotExistException(recipe.getRecipeId());
		}
		String logData = "modified recipe " + recipe.getTitle();
		EventLog eventLog = new EventLog(EventType.EVENT_RECIPE_ADMINISTRATION, logData);
		publisher.publishEvent(new RecipeManagerEvent(this, eventLog));
		
		return recipeRepository.save(recipe);
	}
	
	public Recipe get(String id) {
		if (! recipeRepository.exists(id)) {
			throw new RecipeDoesNotExistException(id);
		}
		return recipeRepository.findOne(id);
	}
}
