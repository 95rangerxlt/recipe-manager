package com.internal.recipes.service;

public final class EventLogDoesNotExistException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE_FORMAT = "EventLog with id '%s' does not exist";

    public EventLogDoesNotExistException(String eventLogId) {
        super(String.format(MESSAGE_FORMAT, eventLogId));
    }
}
