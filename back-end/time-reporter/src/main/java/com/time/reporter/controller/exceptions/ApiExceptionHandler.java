package com.time.reporter.controller.exceptions;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Priority;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.time.reporter.config.jwt.InvalidJwtAuthenticationException;
import com.time.reporter.domain.exceptions.PasswordNotAllowedException;
import com.time.reporter.domain.exceptions.RoleDoesNotExistException;
import com.time.reporter.domain.exceptions.UserAlreadyExistException;
import com.time.reporter.domain.exceptions.UserDoesNotExistException;
import com.time.reporter.domain.exceptions.UserInvalidDataException;

@ControllerAdvice
@Priority(1)
public class ApiExceptionHandler extends ResponseEntityExceptionHandler implements AuthenticationEntryPoint {
	
	public static final Logger LOGGER_EXCEPTION = Logger.getLogger(ApiExceptionHandler.class);
	
	@Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorMessage errorMessage;
        RuntimeException exception =  new ParametersMissmatchException();
        String message = exception.getMessage();
        errorMessage = new ErrorMessage(exception.getClass().getSimpleName(), message);
        return new ResponseEntity<>(errorMessage, headers, status);
    }
	
	@Override
	  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
	                                                                HttpHeaders headers, HttpStatus status,
	                                                                WebRequest request) {	
        ErrorMessage errorMessage;
        RuntimeException exception = new PasswordNotAllowedException();
        String exceptionName = exception.getClass().getSimpleName();
        String message = exception.getMessage();
        errorMessage = new ErrorMessage(exceptionName, message);
        return new ResponseEntity<>(errorMessage, headers, status);
	}
		
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({
		UserInvalidDataException.class, UserDoesNotExistException.class
		, BadCredentialsException.class, RoleDoesNotExistException.class, MethodArgumentTypeMismatchException.class
	})
	@ResponseBody
	public ErrorMessage badRequest(Exception exception) {
		if (exception.getClass().equals(MethodArgumentTypeMismatchException.class)) {
			exception = new ParametersMissmatchException();
		}
		ErrorMessage errorMessage = new ErrorMessage(exception);
		LOGGER_EXCEPTION.error(errorMessage.getDescription());
		return errorMessage;
	}
	
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler({
		DisabledException.class, AccessDeniedException.class
	})
	@ResponseBody
	public ErrorMessage forbidden(Exception exception) {
		ErrorMessage errorMessage = new ErrorMessage(exception);
		LOGGER_EXCEPTION.error(errorMessage.getDescription());
		return errorMessage;
	}
	
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler({
		InvalidJwtAuthenticationException.class
	})
	@ResponseBody
	public ErrorMessage unauthorized(Exception exception) {
		ErrorMessage errorMessage = new ErrorMessage(exception);
		LOGGER_EXCEPTION.error(errorMessage.getDescription());
		return errorMessage;
	}
	
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler({
		UserAlreadyExistException.class
	})
	@ResponseBody
	public ErrorMessage conflict(Exception exception) {
		ErrorMessage errorMessage = new ErrorMessage(exception);
		LOGGER_EXCEPTION.error(errorMessage.getDescription());
		return errorMessage;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		LOGGER_EXCEPTION.error(authException.getMessage());
		returnUnauthorizedResponse(response, authException);
		
	}
	
	public void returnUnauthorizedResponse(HttpServletResponse response, RuntimeException ex) throws IOException {
		Map<String, String> errorResponse = new LinkedHashMap<>();
    	ErrorMessage errorMessage;
        errorMessage = new ErrorMessage(ex);
		errorResponse.put("error", errorMessage.getError());
		errorResponse.put("description", errorMessage.getDescription());
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		ServletOutputStream writer = response.getOutputStream();
		writer.println(new ObjectMapper().writeValueAsString(errorResponse));
		writer.close();
    }

	

}
