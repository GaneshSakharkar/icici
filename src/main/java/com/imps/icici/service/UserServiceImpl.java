package com.imps.icici.service;

import java.net.URLEncoder;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.imps.icici.apiresponse.ApiResponse;
import com.imps.icici.dto.UserDto;
import com.imps.icici.emailservice.EmailService;
import com.imps.icici.entity.UserAddressEntity;
import com.imps.icici.entity.UserEntity;
import com.imps.icici.entity.UserOtpEntity;
import com.imps.icici.mapper.CustomeMapper;
import com.imps.icici.repository.RoleRepository;
import com.imps.icici.repository.UserAddressRepo;
import com.imps.icici.repository.UserOtpReposiroty;
import com.imps.icici.repository.UserRepository;
import com.imps.icici.security.CustomUserDetailService;
import com.imps.icici.security.JwtUtil;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService{
	
	
	private static Logger logger = LogManager.getLogger(UserServiceImpl.class);
	
	
	@Autowired
	private UserRepository oUserRepository;
	
	@Autowired
	private PasswordEncoder oPasswordEncoder;
	
	@Autowired
	private CustomeMapper oCustomeMapper;
	
	@Autowired
	private CustomUserDetailService oCustomUserDetailService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private RoleRepository oRoleRepository;
	
	@Autowired
	private EmailService oEmailService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserOtpReposiroty oUserOtpReposiroty;
	
	@Autowired
	private UserAddressRepo oUserAddressRepo;

	@Transactional
	@Override
	public ApiResponse<UserDto> userRegister(UserDto oUserDto) {
		
		
		String subject = "USER Reg OTP Verify.";
		
		Optional<UserEntity> user = oUserRepository.findByUsername(oUserDto.getUsername());
		
		if(user.isPresent()) {
			logger.info("User already exist with username:{}", oUserDto.getUsername());
			throw new RuntimeException("User already exist with username: "+ oUserDto.getUsername());
		}
		UserEntity entity = new UserEntity();
		
		entity.setUsername(oUserDto.getUsername());
		entity.setPassword(oPasswordEncoder.encode(oUserDto.getPassword()));
		entity.setEmail(oUserDto.getEmail());
		
		UserAddressEntity addressEntity = new UserAddressEntity();
		addressEntity.setMobile(oUserDto.getMobile());
		addressEntity.setFirstName(oUserDto.getFirstName());
		addressEntity.setLastName(oUserDto.getLastName());
		addressEntity.setPanNumber(oUserDto.getPanNumber());
		addressEntity.setAddharNumber(oUserDto.getAddharNumber());
		addressEntity.setCity(oUserDto.getCity());
		addressEntity.setState(oUserDto.getState());
		addressEntity.setPincode(oUserDto.getPincode());
	
		entity.setAddress(addressEntity);
		
		addressEntity.setUsers(entity);
		
		com.imps.icici.entity.RoleEntity role = oRoleRepository.findByRoleName("USER")
				.orElseThrow(()-> new RuntimeException("Role Not found : USER"));
		
		Set<com.imps.icici.entity.RoleEntity> set = new HashSet<com.imps.icici.entity.RoleEntity>();
		set.add(role);
		
		entity.setRoles(set);
		
		String otp = otpGenerator();
		
		boolean isEmailSend = oEmailService.emailSend(subject, oUserDto.getEmail(),"Your One Time Password : " + otp);
		UserEntity userEntity=null;
		
		if(isEmailSend) {
			userEntity = oUserRepository.save(entity);
			
			UserOtpEntity otpEntity = new UserOtpEntity();
			otpEntity.setEmailVerify(false);
			otpEntity.setOtp(otp);
			otpEntity.setUser(userEntity);
			oUserOtpReposiroty.save(otpEntity);
			
		}
		
		UserDto userDto = new UserDto();
		userDto.setId(userEntity.getUserId());
		userDto.setUsername(userEntity.getUsername());
		userDto.setRoles(userEntity.getRoles().toString());
		userDto.setEmail(userEntity.getEmail());
		
		ApiResponse<UserDto> response = new ApiResponse<>();
		response.setMessage("User initiate request for registering please wait for approval.");
		response.setStatusCode(200);
		response.setTimestamp(LocalDateTime.now());
		response.setData(userDto);
		logger.info("UserRegister API Respone:{}", response);
		
		return response;
	}

	@Override
	public UserDto userLogin(UserDto oUserDto) {
	    try {
	        authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(oUserDto.getUsername(), oUserDto.getPassword())
	        );
	    } catch (BadCredentialsException e) {
	        throw new RuntimeException("Invalid username or password");
	    }

	    UserDetails userDetails = oCustomUserDetailService.loadUserByUsername(oUserDto.getUsername());

	    String token = jwtUtil.generateToken(userDetails.getUsername());
	    
	    UserDto response = new UserDto();
	    response.setUsername(userDetails.getUsername());
	    response.setEmail(oUserRepository.findByUsername(userDetails.getUsername()).get().getEmail());
	    response.setToken(token); 
	    return response;
	}
	
	private static String otpGenerator() {
		String numbers = "012356789";
		SecureRandom random = new SecureRandom();
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<6;i++) {
			builder = builder.append(random.nextInt(numbers.length()));
		}
		return builder.toString();
	}

	@Override
	public ApiResponse<UserDto> adminRegister(UserDto oUserDto) {

		
		String subject = "ADMIN Reg OTP Verify.";
		String text= null;
		
		Optional<UserEntity> user = oUserRepository.findByUsername(oUserDto.getUsername());
		
		if(user.isPresent()) {
			throw new RuntimeException("User already exist with username: "+ oUserDto.getUsername());
		}
		UserEntity entity = new UserEntity();
		entity.setUsername(oUserDto.getUsername());
		entity.setPassword(oPasswordEncoder.encode(oUserDto.getPassword()));
		entity.setEmail(oUserDto.getEmail());
		//entity.setRoles(oUserDto.getRoles()!=null ? oUserDto.getRoles() : "USER");
		
		com.imps.icici.entity.RoleEntity role = oRoleRepository.findByRoleName("ADMIN")
				.orElseThrow(()-> new RuntimeException("Role Not found : ADMIN"));
		
		Set<com.imps.icici.entity.RoleEntity> set = new HashSet<com.imps.icici.entity.RoleEntity>();
		set.add(role);
		
		entity.setRoles(set);
		
		String otp = otpGenerator();
		
		boolean isEmailSend = oEmailService.emailSend(subject, oUserDto.getEmail(),"Your One Time Password : " + otp);
		UserEntity userEntity=null;
		
		if(isEmailSend) {
			userEntity = oUserRepository.save(entity);
			
			UserOtpEntity otpEntity = new UserOtpEntity();
			otpEntity.setEmailVerify(false);
			otpEntity.setOtp(otp);
			otpEntity.setUser(userEntity);
			oUserOtpReposiroty.save(otpEntity);
			
		}
		
		UserDto userDto = new UserDto();
		userDto.setId(userEntity.getUserId());
		userDto.setUsername(userEntity.getUsername());
		userDto.setRoles(userEntity.getRoles().toString());
		userDto.setEmail(userEntity.getEmail());
		
		ApiResponse<UserDto> response = new ApiResponse<>();
		response.setMessage("User initiate request for registering please wait for approval.");
		response.setStatusCode(200);
		response.setTimestamp(LocalDateTime.now());
		response.setData(userDto);
		return response;
	
	}

	@Override
	public ApiResponse<?> verifyOtp(String otp, long userId) {

		if(otp==null) {
			throw new RuntimeException("Please enter the otp...!");
		}
		
		UserEntity user2 = oUserRepository.findById(userId).orElseThrow(()-> new RuntimeException("User does not exist..!"));
		long otpId = user2.getOtp().getOtp_id();
		
		Optional<UserOtpEntity> user = oUserOtpReposiroty.findById(otpId);
		
		ApiResponse<?> apiResponse = new ApiResponse<>();
	
		if(user.isPresent()) {
			UserOtpEntity otpData = user.get();
			if(otpData.getOtp().equalsIgnoreCase(otp)) {
				if(LocalDateTime.now().isBefore(otpData.getExpityTime())) {
					otpData.setEmailVerify(true);
					oUserOtpReposiroty.save(otpData);
					apiResponse.setMessage("OTP verfiy Successfully..");
					apiResponse.setStatusCode(200);
					apiResponse.setTimestamp(LocalDateTime.now());
					
				}else {
					
				}
			}else {
				
			}
		}
		
		return apiResponse;
	}

	@Override
	public String generateApiKey() {
		SecureRandom random = new SecureRandom();
		byte[] apiByte = new byte[32];  // 32 
		random.nextBytes(apiByte);
		return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(apiByte);
	}


}
