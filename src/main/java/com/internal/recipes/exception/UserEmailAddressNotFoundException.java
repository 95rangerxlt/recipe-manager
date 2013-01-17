package com.internal.recipes.exception;

public class UserEmailAddressNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String MESSAGE_FORMAT = "No User found for email address '%s'";
	
	public UserEmailAddressNotFoundException(String userEmailAddress) {
		super(String.format(MESSAGE_FORMAT, userEmailAddress));
	}
}