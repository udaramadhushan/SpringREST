package com.restfulspring.app.ws.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.restfulspring.app.ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {

	
	UserDto createUser(UserDto user);
	
	UserDto getUser(String email);
}
