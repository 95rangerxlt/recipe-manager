package com.internal.recipes.event;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.internal.recipes.domain.RecipeManagerEvent;

@Component
public class RecipeEventListener implements ApplicationListener<RecipeManagerEvent> {

	public void onApplicationEvent(RecipeManagerEvent event) {

		System.out.println("got an event!!!" + System.currentTimeMillis());
		System.out.println("the user was: " + SecurityContextHolder.getContext().getAuthentication().getName());
	}

}
