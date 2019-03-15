package com.time.reporter.domain.exceptions;

public class UserNotAllowedToException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -633142353965517259L;
	
	public static final String MESSAGE = "Logged user has not authority to perform the action";
	
	public UserNotAllowedToException() {
		super(MESSAGE);
	}
	

}
