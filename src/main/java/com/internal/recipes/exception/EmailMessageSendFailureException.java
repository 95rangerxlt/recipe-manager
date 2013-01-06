package com.internal.recipes.exception;

public class EmailMessageSendFailureException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String MESSAGE_FORMAT = "EmailMessage Send Failure, error: %s";
	
	public EmailMessageSendFailureException(String errorMessage) {
		super(String.format(MESSAGE_FORMAT, errorMessage));
	}

}
