package com.time.reporter.controller.exceptions;

import org.jboss.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.time.reporter.config.jwt.InvalidJwtAuthenticationException;
import com.time.reporter.domain.exceptions.UserDoesNotExistException;
import com.time.reporter.domain.exceptions.UserNotModifiableException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	public static final Logger LOGGER_EXCEPTION = Logger.getLogger(ApiExceptionHandler.class);
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({
		UserNotModifiableException.class, UserDoesNotExistException.class, InvalidJwtAuthenticationException.class, DisabledException.class, BadCredentialsException.class
	})
	@ResponseBody
	public ErrorMessage badRequest(Exception exception) {
		ErrorMessage errorMessage = new ErrorMessage(exception);
		LOGGER_EXCEPTION.error(errorMessage);
		return errorMessage;
	}

}
