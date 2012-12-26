package com.internal.recipes.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class Recipe {

	@Id
	private String recipeId;
	
	private String contributerUserName;
	private String title;
	private String description;
	private String url;
	private String notes;
	
	public Recipe(String title, String description, String url, String notes) {
		this.title = title;
		this.description = description;
		this.url = url;
		this.notes = notes;
	}

	public Recipe() {
	}

	public String getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(final String recipeId) {
		this.recipeId = recipeId;
	}
	public String getContributerUserName() {
		return this.contributerUserName;
	}
	public String getTitle() {
		return title;
	}
	public void setContributerUserName(String cun) {
		this.contributerUserName = cun;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
}
