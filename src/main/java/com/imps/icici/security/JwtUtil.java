package com.imps.icici.security;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	private static final String SECRET_KEY="b67ebfa7ce00e39ce98c456e3701d133";
	
	private Key getSignKey() {
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	}
	
	// Token Generation
	
	public String generateToken(String username) {
		
		return Jwts.builder().setSubject(username)
		.setIssuedAt(new Date(System.currentTimeMillis()))
		.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))   // 1 hour
		.signWith(getSignKey() , SignatureAlgorithm.HS256)
		.compact();
	}
	
	public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

	
	public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		
		final Claims claims = Jwts.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
		return claimResolver.apply(claims);
	}
	
	 public boolean validateToken(String token, UserDetails userDetails) {
	        final String username = extractUsername(token);
	        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	    }
	 
	 private boolean isTokenExpired(String token) {
	        return extractClaim(token, Claims::getExpiration).before(new Date());
	    }
	 
	 
}
