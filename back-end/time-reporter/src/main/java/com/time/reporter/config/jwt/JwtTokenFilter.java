package com.time.reporter.config.jwt;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.time.reporter.controller.exceptions.ErrorMessage;
import com.time.reporter.domain.exceptions.UserDoesNotExistException;

public class JwtTokenFilter extends GenericFilterBean {

    private JwtTokenProvider jwtTokenProvider;
    
	 public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
	        this.jwtTokenProvider = jwtTokenProvider;
	 }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
        throws IOException, ServletException {    	
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
        try {
        	if (token != null) {
            	jwtTokenProvider.validateDateToken(token);
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
		} catch (UserDoesNotExistException | InvalidJwtAuthenticationException | DisabledException e) {
		    Map<String, String> errorResponse = new LinkedHashMap<>();
	    	ErrorMessage errorMessage;
	        errorMessage = new ErrorMessage(e);
			errorResponse.put("error", errorMessage.getError());
			errorResponse.put("description", errorMessage.getDescription());
			res.getWriter().write(convertObjectToJson(errorResponse));
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
