package com.restfulspring.app.ws.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restfulspring.app.ws.UserRepository;
import com.restfulspring.app.ws.io.entity.UserEntity;
import com.restfulspring.app.ws.service.UserService;
import com.restfulspring.app.ws.shared.dto.UserDto;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	

	public UserDto createUser(UserDto user) {
		UserEntity userEntity = new UserEntity();
		

		
		BeanUtils.copyProperties(user, userEntity);
		userEntity.setEncryptedPassword("testpw");
		userEntity.setUserId("testUserId");
	
		UserEntity storedUserDetails = userRepository.save(userEntity);
		
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties( storedUserDetails , returnValue);
		
		
		return returnValue;
	}

	
	
}
