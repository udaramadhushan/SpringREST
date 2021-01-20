package com.restfulspring.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.restfulspring.app.ws.io.entity.PasswordResetTokenEntity;
import com.restfulspring.app.ws.io.entity.UserEntity;
import com.restfulspring.app.ws.io.repositories.PasswordResetTokenRepository;
import com.restfulspring.app.ws.io.repositories.UserRepository;
import com.restfulspring.app.ws.service.UserService;
import com.restfulspring.app.ws.shared.AmazonSES;
import com.restfulspring.app.ws.shared.Utils;
import com.restfulspring.app.ws.shared.dto.AddressDto;
import com.restfulspring.app.ws.shared.dto.UserDto;




@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	Utils utils;
	
	@Autowired
	BCryptPasswordEncoder  bCryptPasswordEncoder;
	
	
	@Autowired
	PasswordResetTokenRepository passwordTokenRepository;
	

	public UserDto createUser(UserDto user) {
		
		
		if(userRepository.findByEmail(user.getEmail()) != null) throw new RuntimeException("record already exsists");
		
		
		for(int i=0; i<user.getAddresses().size(); i++) {
			
			AddressDto address = user.getAddresses().get(i);
			address.setUserDetails(user);
			address.setAddressId(utils.generateAddressId(30));
			user.getAddresses().set(i, address);
			
		}
		

		
		//BeanUtils.copyProperties(user, userEntity); //when we copy the user to userEntity the userID and the encrypted pw becomes null 
		
		ModelMapper modelMapper = new ModelMapper();
		
		UserEntity userEntity = modelMapper.map(user, UserEntity.class);
		
		
		String publicUserId = utils.generateUserId(30);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userEntity.setUserId(publicUserId);
		
		userEntity.setEmailVerficationToken(utils.generateEmailVerificationToken(publicUserId));
	
		
		UserEntity storedUserDetails = userRepository.save(userEntity);
		
		 
		//BeanUtils.copyProperties( storedUserDetails , returnValue);
		
	    UserDto returnValue = modelMapper.map(storedUserDetails, UserDto.class);
	    
	    
	    //send an email using amazon SES
	    new AmazonSES().verifyEmail(returnValue);
		
		

		return returnValue;
	}


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);
		
		if(userEntity == null) throw new UsernameNotFoundException(email);
		
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),userEntity.getEmailVerificationStatus(),true,true,true, new ArrayList <>());
		//return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList <>());
	}


	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		
		if(userEntity == null) throw new UsernameNotFoundException(email);
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity , returnValue);
		return returnValue;
	}


	@Override
	public UserDto getUserByUserId(String userId) {
		UserDto returnValue = new UserDto();
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if(userEntity == null) throw new UsernameNotFoundException(userId);
		BeanUtils.copyProperties(userEntity , returnValue);
		
		return returnValue;
		
	}


	@Override
	public UserDto updateUser(String userId, UserDto user) {
		UserDto returnValue = new UserDto();
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		if(userEntity == null) throw new UsernameNotFoundException(userId);
		
		
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		
		UserEntity updatedUser = userRepository.save(userEntity);
		
		BeanUtils.copyProperties(updatedUser  , returnValue);
		
		return returnValue;
	}


	@Override
	public void deleteUser(String userId) {
			
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		if( userEntity == null) throw new UsernameNotFoundException(userId);
		
		userRepository.delete(userEntity);
		
		
	}


	@Override
	public List<UserDto> getUsers(int page, int limit) {
		List<UserDto> returnValue = new ArrayList<>();
		
		if(page>0) page = page-1;
		
		Pageable pagebleRequest = PageRequest.of(page, limit);
		Page<UserEntity> usersPage=   userRepository.findAll(pagebleRequest);
		List<UserEntity> users = usersPage.getContent();
		
		for(UserEntity userEntity : users) {
			
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(userEntity, userDto);
			returnValue.add(userDto);
		}
		
		
		
		return returnValue;
	}


	@Override
	public boolean verifyEmailToken(String token) {
		boolean returnValue = false;
		
		UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);
		
		if(userEntity != null) {
			boolean hasTokenExpired = Utils.hasTokenExpired(token);
			if(!hasTokenExpired) {
				userEntity.setEmailVerficationToken(null);
				userEntity.setEmailVerificationStatus(Boolean.TRUE);
				userRepository.save(userEntity);
				returnValue= true;
			}
		}
		
		
		
		return returnValue;
	}


	@Override
	public boolean requestPasswordReset(String email) {
	boolean returnValue =  false;
	UserEntity userEntity = userRepository.findByEmail(email);
	
	
	if(userEntity == null) {
		
		return returnValue;
	}
	
	
	String token =new  Utils().generatePasswordResetToken(userEntity.getUserId());
	
	PasswordResetTokenEntity  passwordResetTokenEntity  = new PasswordResetTokenEntity();
	
	passwordResetTokenEntity.setToken(token);
	passwordResetTokenEntity.setUserDetails(userEntity);
	passwordTokenRepository.save(passwordResetTokenEntity);
	
	
	returnValue = new AmazonSES().sendPasswordResetRequest(userEntity.getFirstName(), userEntity.getEmail(), token);
	
	
	
	
	
	
		return returnValue;
	}

	
	
}
