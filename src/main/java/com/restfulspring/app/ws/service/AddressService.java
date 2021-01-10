package com.restfulspring.app.ws.service;

import java.util.List;

import com.restfulspring.app.ws.shared.dto.AddressDto;

public interface AddressService {
	
	List<AddressDto> getAddresses(String userId);
	
	

}
