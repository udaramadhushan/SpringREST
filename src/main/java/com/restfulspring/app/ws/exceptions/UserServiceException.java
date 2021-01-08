package com.restfulspring.app.ws.exceptions;

public class UserServiceException extends RuntimeException {

	
	
	private static final long serialVersionUID = 5298720612893977173L;

	public UserServiceException (String message) {
		
		super(message);
	}
}
