package com.restfulspring.app.ws.shared;

import java.security.SecureRandom;

import java.util.Random;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.restfulspring.app.ws.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class Utils {

	private final Random RANDOM = new SecureRandom();
	private final String ALPHABET = "1234567890abcdefghijklmnopqstruvwxyz";
	
	private final int ITERATIONS = 1000;
	private final int KEY_LENGTH = 256;
	
	
	public String generateUserId(int length) {
		return generateRandomString(length);
	}
	
	
	public String generateAddressId(int length) {
		return generateRandomString(length);
	}
	private String generateRandomString(int length) {
		
		StringBuilder returnValue = new StringBuilder(length);
		
			for (int i=0 ; i<length; i++) {
		
				returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		
			}
			return new String(returnValue);
	}


	public static boolean hasTokenExpired(String token) {
		Claims claims = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret()).parseClaimsJws(token).getBody();
		
		Date tokenExpirationDate = claims.getExpiration();
		Date todayDate = new Date();
		
		return tokenExpirationDate.before(todayDate);
		
		
	}
	
	public String generateEmailVerificationToken(String userId) {
		
		String token = Jwts.builder().setSubject(userId)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
				.compact();
		return token;
				
	
	}
	
	
	public String generatePasswordResetToken(String userId) {
		
		String token = Jwts.builder().setSubject(userId)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
				.compact();
		return token;
		
		
	}
	
	
	}

