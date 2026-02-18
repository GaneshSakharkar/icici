package com.imps.icici.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.imps.icici.apiresponse.ApiResponse;
import com.imps.icici.dto.AdminRegDTO;
import com.imps.icici.dto.UserDto;
import com.imps.icici.entity.AdminEntity;
import com.imps.icici.entity.IfscBranchCode;
import com.imps.icici.entity.RoleEntity;
import com.imps.icici.entity.UserAccountDetails;
import com.imps.icici.entity.UserAddressEntity;
import com.imps.icici.entity.UserEntity;
import com.imps.icici.exception.ResourceNotFoundException;
import com.imps.icici.repository.AdminRepository;
import com.imps.icici.repository.IfscRepository;
import com.imps.icici.repository.UserAccountDetailsRepo;
import com.imps.icici.repository.UserAddressRepo;
import com.imps.icici.repository.UserRepository;
import com.imps.icici.util.AdminMapper;

@Service
public class AdminSericeImpl implements AdminService{
	
	@Autowired
	private AdminMapper oAdminMapper;
	
	@Autowired
	private AdminRepository oAdminRepository;
	
	@Autowired
	private PasswordEncoder oPasswordEncoder;
	
	@Autowired
	private UserRepository oUserRepository;
	
	@Autowired
	private IfscRepository oIfscRepository;
	
	@Autowired
	private UserAccountDetailsRepo oAccountDetailsRepo;

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

	@Override
	public ApiResponse<List<UserDto>> fetchAllUsers() {
		
	    List<UserEntity> users = oUserRepository.findAll();
		if(users.isEmpty() || users==null) {
			throw new ResourceNotFoundException("User data not exist in tables.");
		}

		List<UserDto> userDtoList = new ArrayList<>();
		
		for(UserEntity each : users) {
			
			Set<Set<RoleEntity>> roles = Collections.singleton(each.getRoles());
			
			//RoleEntity role = new RoleEntity();
			
			for(Set<RoleEntity> role : roles) {
				for(RoleEntity roleName: role) {
					if(roleName.getRoleName().equalsIgnoreCase("USER")) {
						UserDto userDto = new UserDto();
						userDto.setId(each.getUserId());
						userDto.setEmail(each.getEmail());
						userDto.setUsername(each.getUsername());
						userDto.setActive(each.isActive());
						userDtoList.add(userDto);
					}
				}
			}
		}
		
		ApiResponse<List<UserDto>> apiResponse = new ApiResponse<>();
		apiResponse.setMessage("User data");
		apiResponse.setStatusCode(200);
		apiResponse.setTimestamp(LocalDateTime.now());
		apiResponse.setData(userDtoList);
		return apiResponse;
	}

	@Override
	public ApiResponse<Map<String, Object>> approveUserRequest(long userId, boolean isActive) {
		
	
		
		Optional<UserEntity> userData = oUserRepository.findById(userId);
		
		if(!userData.isPresent()) {
			throw new ResourceNotFoundException("User not found...");
		}
		
		UserEntity userUpdate = userData.get();
		userUpdate.setActive(true);
		
		UserEntity saveData = oUserRepository.save(userUpdate);
		
		
		if(saveData.isActive()) {
			
			long pincode = saveData.getAddress().getPincode();
			
			IfscBranchCode ifscDetails = oIfscRepository.findByPincode(pincode)
					.orElseThrow(()-> new ResourceNotFoundException("IFSC Details Not Found Exception....!"));
			String ifsc = ifscDetails.getIfsc();
			
			String accountNumber = generateAccountNumber(ifsc);
			
			if(accountNumber==null && accountNumber.isEmpty()) {
				throw new RuntimeException("Account Number generation failed..!");
			}
		
			Optional<UserAccountDetails> accountDetais = oAccountDetailsRepo.findByAccountNumber(accountNumber);
			if(!accountDetais.isPresent()) {
				UserAccountDetails newAccount = new UserAccountDetails();
				newAccount.setAccountNumber(accountNumber);
				newAccount.setAccountType("SAVING");
				newAccount.setIfsc(ifscDetails.getIfsc());
				newAccount.setVpa_id("ganesh123@okhdfc");
				newAccount.setCrn("12346567899");
				oAccountDetailsRepo.save(newAccount);
			}else {
				throw new RuntimeException("Account number already Exist...!");
			}
			
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("Active", saveData.isActive());
		map.put("userId", saveData.getUserId());
		map.put("email", saveData.getEmail());
		
		ApiResponse<Map<String, Object>> response = new ApiResponse<>();
		response.setMessage("User Active..");
		response.setStatusCode(200);
		response.setTimestamp(LocalDateTime.now());
		response.setData(map);
		return response;
	}
	
	
	private static String generateAccountNumber(String ifsc) {
		String branchValue = ifsc.substring(6);		
		String year = String.valueOf(Year.now().getValue());
		int random = 100000 + new Random().nextInt(900000);
		return branchValue + year + random;
	}
	

}
