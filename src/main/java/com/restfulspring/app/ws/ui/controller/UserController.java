package com.restfulspring.app.ws.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restfulspring.app.ws.exceptions.UserServiceException;
import com.restfulspring.app.ws.service.AddressService;
import com.restfulspring.app.ws.service.UserService;
import com.restfulspring.app.ws.shared.dto.AddressDto;
import com.restfulspring.app.ws.shared.dto.UserDto;
import com.restfulspring.app.ws.ui.model.request.UserDetailsRequestModel;
import com.restfulspring.app.ws.ui.model.response.AddressesRest;
import com.restfulspring.app.ws.ui.model.response.ErrorMessages;
import com.restfulspring.app.ws.ui.model.response.OperationStatusModel;
import com.restfulspring.app.ws.ui.model.response.UserRest;




@RestController
@RequestMapping("/users")

public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressesService;
	
	@Autowired
	AddressService addressService;
	

	
	@GetMapping(path="/{id}",
				produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserRest getUser(@PathVariable String id){
		
		UserRest returnValue = new UserRest();
		
		UserDto userDto = userService.getUserByUserId(id);
		BeanUtils.copyProperties(userDto, returnValue);
		
		return returnValue;
	}
	
	
	@GetMapping(path="/{id}/addresses",
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
public List<AddressesRest> getUserAddresses(@PathVariable String id){
	
		List<AddressesRest> returnValue = new ArrayList<>();
	
	List<AddressDto> addressDto = addressesService.getAddresses(id);
	
	if(addressDto != null && !addressDto.isEmpty()) {
	Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
		returnValue = new ModelMapper().map(addressDto,listType);
	
	}
	return returnValue;
}
	
	
	@GetMapping(path="/{id}/addresses/{addressId}",
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
public AddressesRest getUserAddress(@PathVariable String addressId){
	
		AddressDto addressDto = addressService.getAddress(addressId);
		
		ModelMapper modelMapper = new ModelMapper();
		//WebMvcLinkBuilder addressLink = WebMvcLinkBuilder.linkTo(UserController.class).slash("addresses").slash(addressId)
		
		return modelMapper.map(addressDto, AddressesRest.class);
}
	
	
	@PostMapping(path="/register",
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		
		UserRest returnValue = new UserRest();
		
		
		if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		
		
		//UserDto userDto = new UserDto();
		//BeanUtils.copyProperties(userDetails, userDto);
		
		ModelMapper modelMapper = new ModelMapper();
		
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);

		
		UserDto createdUser = userService.createUser(userDto);
		returnValue = modelMapper.map(createdUser,UserRest.class);
		
		return returnValue;
		
	}
	
	
	@PutMapping(path="/{id}",
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserRest updateUser(@RequestBody UserDetailsRequestModel userDetails, @PathVariable String id) {
		UserRest returnValue = new UserRest();
		
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		
		UserDto updatedUser = userService.updateUser(id, userDto);
		BeanUtils.copyProperties(updatedUser, returnValue);
		
		return returnValue;
	}
	
	@DeleteMapping(path="/{id}",
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public OperationStatusModel deleteUser(@PathVariable String id) {
		
		OperationStatusModel returnValue =  new OperationStatusModel();
		returnValue.setOperationName("Delete");
		returnValue.setOperationResult("Success");
		
		userService.deleteUser(id);
		return returnValue;
		
	}
	
	@GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public List<UserRest> getUsers(@RequestParam(value="page", defaultValue="0")int page,@RequestParam(value="limit", defaultValue="25")int limit){
		
		List <UserRest> returnValue = new ArrayList<>();
		List<UserDto> users= userService.getUsers(page,limit);
		
		for(UserDto userDto : users) {
			
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
			
		}
	
		
		return returnValue;
		
	}
	
	
	@GetMapping(path="/email-verification",produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public OperationStatusModel verifyEmailToken(@RequestParam(value="token")String token){
		
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());
		
		boolean isVerified = userService.verifyEmailToken(token);
		
		if(isVerified) {
			
			returnValue.setOperationResult("success");
		}else {
			returnValue.setOperationResult("failed");
		}
		
	
		
		return returnValue;
		
	}
	
	
	
	
}
