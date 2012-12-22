package com.internal.recipes.exception;

public final class RecipeDoesNotExistException extends RuntimeException {
    
	private static final long serialVersionUID = 8462359616920828380L;
	private static final String MESSAGE_FORMAT = "Recipe with id '%s' does not exist";

    public RecipeDoesNotExistException(String recipeId) {
        super(String.format(MESSAGE_FORMAT, recipeId));
    }
}
