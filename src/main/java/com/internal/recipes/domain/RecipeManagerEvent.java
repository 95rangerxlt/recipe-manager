package com.internal.recipes.domain;

import org.springframework.context.ApplicationEvent;

public class RecipeManagerEvent extends ApplicationEvent{

	private EventLog eventLog;
	private static final long serialVersionUID = 1L;

	public RecipeManagerEvent(Object source, EventLog eventLog) {
		super(source);
		this.eventLog = eventLog;
	}

	public EventLog getEventLog() {
		return eventLog;
	}

	public void setEventLog(EventLog eventLog) {
		this.eventLog = eventLog;
	}

}
