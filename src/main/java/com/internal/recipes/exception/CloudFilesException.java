package com.internal.recipes.exception;

public class CloudFilesException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String MESSAGE_FORMAT = "CloudFiles Operation Failure, error: %s";
	
	public CloudFilesException(String errorMessage) {
		super(String.format(MESSAGE_FORMAT, errorMessage));
	}
}
