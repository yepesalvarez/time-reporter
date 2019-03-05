package com.time.reporter.controller.exceptions;

import javax.annotation.Priority;

import org.jboss.logging.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.time.reporter.config.jwt.InvalidJwtAuthenticationException;
import com.time.reporter.domain.exceptions.PasswordNotAllowedException;
import com.time.reporter.domain.exceptions.RoleDoesNotExistException;
import com.time.reporter.domain.exceptions.UserAlreadyExistException;
import com.time.reporter.domain.exceptions.UserDoesNotExistException;
import com.time.reporter.domain.exceptions.UserInvalidDataException;

@ControllerAdvice
@Priority(1)
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
	
	@Override
	  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
	                                                                HttpHeaders headers, HttpStatus status,
	                                                                WebRequest request) {	
        ErrorMessage errorMessage;
        String exceptionName =  PasswordNotAllowedException.class.getSimpleName();
        String message = PasswordNotAllowedException.MESSAGE;
        errorMessage = new ErrorMessage(exceptionName, message);
        return new ResponseEntity<>(errorMessage, headers, status);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({
		UserInvalidDataException.class, UserDoesNotExistException.class
		, BadCredentialsException.class, RoleDoesNotExistException.class
	})
	@ResponseBody
	public ErrorMessage badRequest(Exception exception) {
		ErrorMessage errorMessage = new ErrorMessage(exception);
		LOGGER_EXCEPTION.error(errorMessage);
		return errorMessage;
	}
	
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler({
		DisabledException.class, AccessDeniedException.class
	})
	@ResponseBody
	public ErrorMessage forbidden(Exception exception) {
		ErrorMessage errorMessage = new ErrorMessage(exception);
		LOGGER_EXCEPTION.error(errorMessage);
		return errorMessage;
	}
	
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler({
		InvalidJwtAuthenticationException.class
	})
	@ResponseBody
	public ErrorMessage unauthorized(Exception exception) {
		ErrorMessage errorMessage = new ErrorMessage(exception);
		LOGGER_EXCEPTION.error(errorMessage);
		return errorMessage;
	}
	
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler({
		UserAlreadyExistException.class
	})
	@ResponseBody
	public ErrorMessage conflict(Exception exception) {
		ErrorMessage errorMessage = new ErrorMessage(exception);
		LOGGER_EXCEPTION.error(errorMessage);
		return errorMessage;
	}

}
