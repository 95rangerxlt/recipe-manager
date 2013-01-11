package com.internal.recipes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.internal.recipes.domain.EventLog;
import com.internal.recipes.domain.EventType;
import com.internal.recipes.domain.Recipe;
import com.internal.recipes.domain.RecipeManagerEvent;
import com.internal.recipes.exception.RecipeDoesNotExistException;
import com.internal.recipes.repository.RecipeRepository;

@Service
public class RecipeServiceImpl implements RecipeService {
	
	@Autowired
	RecipeRepository recipeRepository;
	
	@Autowired
	ApplicationEventPublisher publisher;
	
	/* (non-Javadoc)
	 * @see com.internal.recipes.service.RecipeService#getAllRecipes()
	 */
	public List<Recipe> getAllRecipes() {
		return recipeRepository.findAll();
	}
	
	/* (non-Javadoc)
	 * @see com.internal.recipes.service.RecipeService#create(com.internal.recipes.domain.Recipe)
	 */
	public Recipe create(Recipe recipe) {
		String logData = "created recipe " + recipe.getTitle();
		EventLog eventLog = new EventLog(EventType.EVENT_RECIPE_CREATED, logData);
		publisher.publishEvent(new RecipeManagerEvent(this, eventLog));
		return recipeRepository.save(recipe);
	}
	
	/* (non-Javadoc)
	 * @see com.internal.recipes.service.RecipeService#delete(com.internal.recipes.domain.Recipe)
	 */
	public Boolean delete(Recipe recipe) {
		if (!recipeRepository.exists(recipe.getRecipeId())) {
			return false;
		}
		String logData = "deleted recipe " + recipe.getTitle();
		EventLog eventLog = new EventLog(EventType.EVENT_RECIPE_DELETED, logData);
		publisher.publishEvent(new RecipeManagerEvent(this, eventLog));
		
		recipeRepository.delete(recipe);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.internal.recipes.service.RecipeService#update(com.internal.recipes.domain.Recipe)
	 */
	public Recipe update(Recipe recipe) {
		if (!recipeRepository.exists(recipe.getRecipeId())) {
			throw new RecipeDoesNotExistException(recipe.getRecipeId());
		}
		String logData = "modified recipe " + recipe.getTitle();
		EventLog eventLog = new EventLog(EventType.EVENT_RECIPE_MODIFIED, logData);
		publisher.publishEvent(new RecipeManagerEvent(this, eventLog));
		
		return recipeRepository.save(recipe);
	}
	
	/* (non-Javadoc)
	 * @see com.internal.recipes.service.RecipeService#get(java.lang.String)
	 */
	public Recipe get(String id) {
		if (! recipeRepository.exists(id)) {
			throw new RecipeDoesNotExistException(id);
		}
		return recipeRepository.findOne(id);
	}
}
