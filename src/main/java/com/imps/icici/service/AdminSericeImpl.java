package com.imps.icici.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.imps.icici.dto.AdminRegDTO;
import com.imps.icici.entity.AdminEntity;
import com.imps.icici.repository.AdminRepository;
import com.imps.icici.util.AdminMapper;

@Service
public class AdminSericeImpl implements AdminService{
	
	@Autowired
	private AdminMapper oAdminMapper;
	
	@Autowired
	private AdminRepository oAdminRepository;
	
	@Autowired
	private PasswordEncoder oPasswordEncoder;

	@Override
	public AdminRegDTO adminRegister(AdminRegDTO oAdminRegDTO) {
		Optional<AdminEntity> valid = oAdminRepository.findByEmail(oAdminRegDTO.getEmail());
		
		Optional<AdminEntity> usernameValid = oAdminRepository.findByUsername(oAdminRegDTO.getUsername());
		
		if(valid.isPresent()) {
			throw new RuntimeException("Email is Already Available..!");
		}
		
		if(usernameValid.isPresent()) {
			throw new RuntimeException("Email is Already Available..!");
		}
		
		
		AdminEntity entity =  oAdminMapper.dtoToEntity(oAdminRegDTO);
		entity.setPassword(oPasswordEncoder.encode(oAdminRegDTO.getPassword()));
		AdminEntity data = oAdminRepository.save(entity);
		return oAdminMapper.enityToDto(data);
	}

}
