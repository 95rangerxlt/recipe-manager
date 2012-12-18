package com.internal.recipes.service;

public class UsernameNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE_FORMAT = "UserName '%s' not found";

	public UsernameNotFoundException(String userName) {
		super(String.format(MESSAGE_FORMAT, userName));
	}
}

