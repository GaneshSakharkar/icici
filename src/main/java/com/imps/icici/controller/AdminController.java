package com.imps.icici.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imps.icici.apiresponse.ApiResponse;
import com.imps.icici.dto.AdminRegDTO;
import com.imps.icici.dto.UserDto;
import com.imps.icici.service.AdminService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/app/v1/admin")
public class AdminController {
	
	
	@Autowired
	private AdminService oAdminService;
	
	@GetMapping("/welcome")
	public String welcome() {
		
		return "Welcome this is admin page, access for admin only...!";
	}
	
	@GetMapping("/fetch/users")
	public ResponseEntity<ApiResponse<List<UserDto>>> fetchAllUsers(){
		ApiResponse<List<UserDto>> response = oAdminService.fetchAllUsers();
		return new ResponseEntity<ApiResponse<List<UserDto>>>(response , HttpStatus.OK);
	}
	
	@PutMapping("/active/user")
	public ResponseEntity<ApiResponse<Map<String,Object>>> approveUserRequest(@RequestBody Map<String,Object> requestBody){
		
		long userId  = Long.parseLong(requestBody.get("userId").toString());
		boolean isActive = requestBody.get("isActive").toString() != null;
		
		ApiResponse<Map<String,Object>> response = oAdminService.approveUserRequest(userId,isActive);
		
		
		
		return new ResponseEntity<ApiResponse<Map<String,Object>>>(response , HttpStatus.OK);
	}
	
	
}
