package com.restfulspring.app.ws.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping("users")

public class UserController {

	@GetMapping
	public String getUser() {
		
		return "get user was called";
		
	}
	
	
	@PostMapping
	public String createUser() {
		
		return "create user was called";
	}
	
	
	@PutMapping
	public String updateUser() {
		return "update user was created";
	}
	
	@DeleteMapping
	public String deleteUser() {
		
		return "delete user was created";
	}
	
	
	
	
	
	
}
