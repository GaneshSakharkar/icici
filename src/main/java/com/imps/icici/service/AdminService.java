package com.imps.icici.service;

import java.util.List;
import java.util.Map;

import com.imps.icici.apiresponse.ApiResponse;
import com.imps.icici.dto.AdminRegDTO;
import com.imps.icici.dto.UserDto;

public interface AdminService {

	AdminRegDTO adminRegister(AdminRegDTO oAdminRegDTO);

	ApiResponse<List<UserDto>> fetchAllUsers();

	ApiResponse<Map<String, Object>> approveUserRequest(long userId, boolean isActive);

}
