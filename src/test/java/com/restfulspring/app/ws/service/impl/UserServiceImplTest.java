package com.restfulspring.app.ws.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.restfulspring.app.ws.io.entity.UserEntity;
import com.restfulspring.app.ws.io.repositories.UserRepository;
import com.restfulspring.app.ws.shared.dto.UserDto;

class UserServiceImplTest {
	
	@InjectMocks
	UserServiceImpl userService;
	
	
	@Mock
	UserRepository userRepository;
	

	
	@BeforeEach
	void setUp() throws Exception {
		
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetUser() {
		
		UserEntity userEntity = new UserEntity();
		
		userEntity.setId(1L);
		userEntity.setFirstName("udara");
		userEntity.setLastName("madhushan");
		userEntity.setEncryptedPassword("sdasjfnrejnfiejnrfjenrf");
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		
		UserDto userDto = userService.getUser("duauhue@dsaji.com");
		
		
		assertNotNull(userDto);
		assertEquals("udara", userDto.getFirstName());
		
		
	}

}
