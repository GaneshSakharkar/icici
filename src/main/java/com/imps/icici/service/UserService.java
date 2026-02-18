package com.imps.icici.service;

import com.imps.icici.apiresponse.ApiResponse;
import com.imps.icici.dto.UserDto;

public interface UserService {

	ApiResponse<UserDto> userRegister(UserDto oUserDto);

	UserDto userLogin(UserDto oUserDto);

	ApiResponse<UserDto> adminRegister(UserDto oUserDto);

	ApiResponse<?> verifyOtp(String otp, long userId);

	String generateApiKey();

}
