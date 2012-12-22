package com.internal.recipes.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.internal.recipes.domain.RecipeManagerEvent;
import com.internal.recipes.repository.EventLogRepository;

@Component
public class RecipeEventListener implements ApplicationListener<RecipeManagerEvent> {
	
	@Autowired
	private EventLogRepository eventLogRepository;

	public void onApplicationEvent(RecipeManagerEvent event) {
		String actor = "System";
		
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			actor = (String) SecurityContextHolder.getContext().getAuthentication().getName();
		}
		
		event.getEventLog().setActor(actor);
		eventLogRepository.save(event.getEventLog());
	}

}
