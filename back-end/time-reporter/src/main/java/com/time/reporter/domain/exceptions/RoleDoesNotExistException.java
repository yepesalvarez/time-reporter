package com.time.reporter.domain.exceptions;

public class RoleDoesNotExistException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1408669135338565922L;
	
	private static final String MESSAGE = "Role does not exist";
	
	public RoleDoesNotExistException() {
		super(MESSAGE);
	}

}
