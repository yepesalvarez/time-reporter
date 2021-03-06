package com.time.reporter.config.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@PropertySource("classpath:application.properties")
@Component
public class JwtTokenProvider {
		
	@Value("${security.jwt.token.secret-key:time-reporter}")
	    private String secretKey = "time-reporter";

	@Value("${security.jwt.token.expire-length:3600000}") //1h
    private long validityInMilliseconds = 3600000; 

	@Autowired
    private UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
    
    public static final Logger LOGGER = Logger.getLogger(JwtTokenProvider.class);

    public void setValidityInMilliseconds(long validityInMilliseconds) {
		this.validityInMilliseconds = validityInMilliseconds;
	}
    
    public String createToken(String username, List<String> roles) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        if (!userDetails.isEnabled()) {
        	throw new DisabledException("User is disabled"); 
        }
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public void validateDateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            boolean tokenIsExpired = claims.getBody().getExpiration().before(new Date());
            if (tokenIsExpired) {
            	LOGGER.error(new InvalidJwtAuthenticationException().getMessage());
                throw new InvalidJwtAuthenticationException(); 
            }           
        } catch (JwtException | IllegalArgumentException e) {
        	LOGGER.error(new InvalidJwtAuthenticationException());
            throw new InvalidJwtAuthenticationException();
        }
    }

}
