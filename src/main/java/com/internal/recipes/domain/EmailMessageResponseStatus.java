package com.internal.recipes.domain;

public class EmailMessageResponseStatus {
	
	private Boolean sucess;
	private String message;
	
	public EmailMessageResponseStatus(Boolean sucess, String message) {
		this.setSucess(sucess);
		this.setMessage(message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getSucess() {
		return sucess;
	}

	public void setSucess(Boolean sucess) {
		this.sucess = sucess;
	}

}
