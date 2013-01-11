package com.internal.recipes.domain;

public enum EventType {
	EVENT_SECURITY ("Security"),
	EVENT_USER_ADMINISTATION ("User Admin"),
	EVENT_USER_CREATED("User created"),
	EVENT_USER_MODIFIED("User modified"),
	EVENT_USER_DELETED("User deleted"),
	EVENT_RECIPE_ADMINISTRATION("Recipe Admin"),
	EVENT_RECIPE_CREATED("Recipe created"),
	EVENT_RECIPE_MODIFIED("Recipe modified"),
	EVENT_RECIPE_DELETED("Recipe deleted");
	
	private final String type;
	
	private EventType(String type)		{this.type = type;}
	
	public String getType()				{return this.type;}
}
