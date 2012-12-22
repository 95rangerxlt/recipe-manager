package com.internal.recipes.domain;

import java.util.Date;

import org.springframework.context.ApplicationEvent;

public class RecipeManagerEvent extends ApplicationEvent{

	private EventLog eventLog;
	private static final long serialVersionUID = 1L;

	public RecipeManagerEvent(Object source, EventLog eventLog) {
		super(source);
		Date eventDate = new Date(this.getTimestamp());
		eventLog.setLogDate(eventDate);
		this.eventLog = eventLog;
	}

	public EventLog getEventLog() {
		return eventLog;
	}

	public void setEventLog(EventLog eventLog) {
		this.eventLog = eventLog;
	}

}
