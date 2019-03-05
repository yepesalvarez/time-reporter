package com.time.reporter.config.jwt;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.time.reporter.controller.exceptions.ErrorMessage;
import com.time.reporter.domain.exceptions.UserDoesNotExistException;

public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;
    
	 public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
	        this.jwtTokenProvider = jwtTokenProvider;
	 }

    @Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
			throws ServletException, IOException {    	
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
        try {
        	if (token != null) {
            	jwtTokenProvider.validateDateToken(token);
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
		} catch (InvalidJwtAuthenticationException | DisabledException | UserDoesNotExistException e) {
	    	Map<String, String> errorResponse = new LinkedHashMap<>();
	    	ErrorMessage errorMessage;
	        errorMessage = new ErrorMessage(e);
			errorResponse.put("error", errorMessage.getError());
			errorResponse.put("description", errorMessage.getDescription());
			res.setStatus(HttpStatus.UNAUTHORIZED.value());
			ServletOutputStream writer = res.getOutputStream();
			writer.println(convertObjectToJson(errorResponse));
			writer.close();
		} 
        	filterChain.doFilter(req, res);
        
    }
    
    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

}
