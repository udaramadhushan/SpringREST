package com.restfulspring.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.restfulspring.app.ws.io.entity.AddressEntity;
import com.restfulspring.app.ws.io.entity.UserEntity;
import com.restfulspring.app.ws.io.repositories.UserRepository;
import com.restfulspring.app.ws.service.AddressService;
import com.restfulspring.app.ws.shared.dto.AddressDto;

public class AddressServiceImpl implements AddressService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AddressRepository addressRepository;
	
	

	@Override
	public List<AddressDto> getAddresses(String userId) {
		List<AddressDto> returnValue = new ArrayList<>();
		ModelMapper modelmapper = new ModelMapper();
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		
		if(userEntity ==  null)  return returnValue;
		
		Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
		
		for(AddressEntity addressEntity: addresses) {
			returnValue.add(modelmapper.map(addressEntity, AddressDto.class));
		}
		
		return returnValue;
	}

}
