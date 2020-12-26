package com.restfulspring.app.ws.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfulspring.app.ws.ui.model.request.UserLoginRequestModel;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	
	private final AuthenticationManager authenticationManager;
	
	public AuthenticationFilter (AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,
													HttpServletResponse res) throws AuthenticationException{
		try {
			UserLoginRequestModel creds = new ObjectMapper().readValue(req.getInputStream(),UserLoginRequestModel.class);
			
			return authenticationManager.authenticate(
					
					new UsernamePasswordAuthenticationToken(
							
							creds.getEmail(),
							creds.getPassword(),
							new ArrayList<>())
							
							
					);
					
		} catch (IOException e) {
			
			throw new RuntimeException(e);
		}
			
	}
	
	@Override
	
	protected void successfulAuthentication(HttpServletRequest req,
													HttpServletResponse res,
													FilterChain chain,
													Authentication auth) throws IOException, ServletException{
		
		String userName =  ((User)auth.getPrincipal()).getUsername();
		
		String token =Jwts.builder()
		
		
	}
	
	
	
}
