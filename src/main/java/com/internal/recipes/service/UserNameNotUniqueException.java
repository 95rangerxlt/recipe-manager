package com.internal.recipes.service;

public final class UserNameNotUniqueException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String MESSAGE_FORMAT = "UserName '%s' already exists";
	
	public UserNameNotUniqueException(String userName) {
		super(String.format(MESSAGE_FORMAT, userName));
	}

}
