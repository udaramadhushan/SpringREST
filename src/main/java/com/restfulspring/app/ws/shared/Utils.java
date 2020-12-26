package com.restfulspring.app.ws.shared;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class Utils {

	private final Random RANDOM = new SecureRandom();
	private final String ALPHABET = "1234567890abcdefghijklmnopqstruvwxyz";
	
	private final int ITERATIONS = 1000;
	private final int KEY_LENGTH = 256;
	
	
	public String generateUserId(int length) {
		return generateRandomString(length);
	}
	
	private String generateRandomString(int length) {
		
		StringBuilder returnValue = new StringBuilder(length);
		
			for (int i=0 ; i<length; i++) {
		
				returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		
			}
			return new String(returnValue);
	}
	
	
	}

