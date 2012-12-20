package com.internal.recipes.domain;

public enum EventType {
	EVENT_SECURITY ("Security"),
	EVENT_USER_ADMINISTATION ("User Admin"),
	EVENT_RECIPE_ADMINISTRATION("Recipe Admin");
	
	private final String type;
	
	private EventType(String type)		{this.type = type;}
	
	public String getType()				{return this.type;}
}
