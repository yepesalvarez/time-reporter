package com.time.reporter.controller.exceptions;

import org.jboss.logging.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.time.reporter.config.jwt.InvalidJwtAuthenticationException;
import com.time.reporter.domain.exceptions.UserAlreadyExistException;
import com.time.reporter.domain.exceptions.UserDoesNotExistException;
import com.time.reporter.domain.exceptions.UserInvalidDataException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	public static final Logger LOGGER_EXCEPTION = Logger.getLogger(ApiExceptionHandler.class);
	
	@Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable mostSpecificCause = ex.getMostSpecificCause();
        ErrorMessage errorMessage;
        String exceptionName =  mostSpecificCause.getClass().getSimpleName();
        String message = mostSpecificCause.getMessage();
        errorMessage = new ErrorMessage(exceptionName, message);
        return new ResponseEntity<>(errorMessage, headers, status);
    }
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({
		UserInvalidDataException.class, UserDoesNotExistException.class, InvalidJwtAuthenticationException.class
		, DisabledException.class, BadCredentialsException.class
	})
	@ResponseBody
	public ErrorMessage badRequest(Exception exception) {
		ErrorMessage errorMessage = new ErrorMessage(exception);
		LOGGER_EXCEPTION.error(errorMessage);
		return errorMessage;
	}
	
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler({
		UserAlreadyExistException.class
	})
	@ResponseBody
	public ErrorMessage repeatedInfo(Exception exception) {
		ErrorMessage errorMessage = new ErrorMessage(exception);
		LOGGER_EXCEPTION.error(errorMessage);
		return errorMessage;
	}
	
	 

}
