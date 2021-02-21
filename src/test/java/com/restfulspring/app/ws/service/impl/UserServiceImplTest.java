package com.restfulspring.app.ws.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.restfulspring.app.ws.io.entity.AddressEntity;
import com.restfulspring.app.ws.io.entity.UserEntity;
import com.restfulspring.app.ws.io.repositories.PasswordResetTokenRepository;
import com.restfulspring.app.ws.io.repositories.UserRepository;
import com.restfulspring.app.ws.shared.AmazonSES;
import com.restfulspring.app.ws.shared.Utils;
import com.restfulspring.app.ws.shared.dto.AddressDto;
import com.restfulspring.app.ws.shared.dto.UserDto;

import aj.org.objectweb.asm.Type;



class UserServiceImplTest {
	
	@InjectMocks
	UserServiceImpl userService;
	
	
	@Mock
	UserRepository userRepository;
	
	
	
	@Mock
	AmazonSES amazonSES;
	
	@Mock
	Utils utils;
	
	@Mock
	BCryptPasswordEncoder  bCryptPasswordEncoder;
	
	
	UserEntity userEntity;
	

	
	@BeforeEach
	void setUp() throws Exception {
		
		MockitoAnnotations.openMocks(this);
		
		 userEntity = new UserEntity();
		
		userEntity.setId(1L);
		userEntity.setFirstName("udara");
		userEntity.setLastName("madhushan");
		userEntity.setUserId("sadsdsadededededa");
		userEntity.setEncryptedPassword("sdasjfnrejnfiejnrfjenrf");
		userEntity.setEmail("test@test.com");
		userEntity.setEmailVerificationToken("sdsdfsdfdfdsfdsfdsfdsfds");
		
		userEntity.setAddresses(getAddressesEntity());
	}

	@Test
	void testGetUser() {
		
	
		
		
		
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		
		UserDto userDto = userService.getUser("duauhue@dsaji.com");
		
		
		assertNotNull(userDto);
		assertEquals("udara", userDto.getFirstName());
		
		
	}
	
	
	@Test
	final void testGetUser_UsernameNotFoundException() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		
		
		
		assertThrows(UsernameNotFoundException.class,
				
				()->{
					
					userService.getUser("test@test.com");
				}
				
				);
		
	}
	
	
	
	
	@Test 
	final void testCreateUser() {
		
		
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		when(utils.generateAddressId(anyInt())).thenReturn("gdsfsfksd");
		when(utils.generateUserId(anyInt())).thenReturn("sdsdfdf");
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn("sdasdsadasdsadsdada");
		when( userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
		Mockito.doNothing().when(amazonSES).verifyEmail(any(UserDto.class));
		
	
		
		UserDto userDto = new UserDto();
		userDto.setAddresses(getAddressessDto());
		userDto.setFirstName("udara");
		userDto.setLastName("madhushan");
		userDto.setPassword("udaram3054");
		userDto.setEmail("udara@1re.com");
	
	    UserDto storedUserDetails = userService.createUser(userDto);
	    assertNotNull(storedUserDetails);
		assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
		assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
		   assertNotNull(storedUserDetails.getUserId());
		  assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());
		verify(utils,times(2)).generateAddressId(30);
		verify(bCryptPasswordEncoder,times(1)).encode("udaram3054");
		
		
		
	}
	
	private List<AddressDto> getAddressessDto(){

		
		AddressDto addressDto = new AddressDto();
		addressDto.setType("shipping");
		addressDto.setCity("mawanella");
		addressDto.setCountry("SL");
		addressDto.setPostalCode("71500");
		addressDto.setStreetName("anwarama");
		
		
		AddressDto billingAddressDto = new AddressDto();
		billingAddressDto.setType("billing");
		billingAddressDto.setCity("mawanella");
		billingAddressDto.setCountry("SL");
		billingAddressDto.setPostalCode("71500");
		billingAddressDto.setStreetName("anwarama");
		
		
		List<AddressDto> addresses = new ArrayList<>();
		addresses.add(addressDto);
		addresses.add(billingAddressDto);
		
		
		return addresses;
		
}
	
	
	private List<AddressEntity> getAddressesEntity(){
		
		List<AddressDto> addresses = getAddressessDto();
		
		java.lang.reflect.Type listType = new TypeToken<List<AddressEntity>>(){}.getType();
		
		return new ModelMapper().map(addresses,listType);
		
		
	}
}






