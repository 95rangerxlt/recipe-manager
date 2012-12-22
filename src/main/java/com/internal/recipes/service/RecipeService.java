package com.internal.recipes.service;

import java.util.List;

import com.internal.recipes.domain.Recipe;

public interface RecipeService {

	List<Recipe> getAllRecipes();

	Recipe create(Recipe recipe);

	Boolean delete(Recipe recipe);

	Recipe update(Recipe recipe);

	Recipe get(String id);

}