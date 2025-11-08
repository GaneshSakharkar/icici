package com.imps.icici.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imps.icici.dto.AdminRegDTO;
import com.imps.icici.service.AdminService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/app/v1/admin")
public class AdminController {

	
	@Autowired
	private AdminService oAdminService;
	
	@PostMapping("/register")
	public ResponseEntity<AdminRegDTO> adminRegister(@Valid @RequestBody AdminRegDTO oAdminRegDTO){
		AdminRegDTO data = oAdminService.adminRegister(oAdminRegDTO);
		
		return new ResponseEntity<AdminRegDTO>(data,HttpStatus.CREATED);
		
	}
}
