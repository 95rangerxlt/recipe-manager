package com.internal.recipes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.internal.recipes.domain.Recipe;
import com.internal.recipes.repository.RecipeRepository;

@Service
public class RecipeService {
	
	@Autowired
	RecipeRepository recipeRepository;
	
	public List<Recipe> getAllRecipes() {
		return recipeRepository.findAll();
	}
	
	public Recipe create(Recipe recipe) {
		return recipeRepository.save(recipe);
	}
	
	public Boolean delete(Recipe recipe) {
		if (!recipeRepository.exists(recipe.getRecipeId())) {
			return false;
		}
		
		recipeRepository.delete(recipe);
		return true;
	}
	
	public Recipe update(Recipe recipe) {
		if (!recipeRepository.exists(recipe.getRecipeId())) {
			throw new RecipeDoesNotExistException(recipe.getRecipeId());
		}
		return recipeRepository.save(recipe);
	}
	
	public Recipe get(String id) {
		if (! recipeRepository.exists(id)) {
			throw new RecipeDoesNotExistException(id);
		}
		return recipeRepository.findOne(id);
	}
}
