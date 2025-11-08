package com.imps.icici.util;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imps.icici.dto.AdminRegDTO;
import com.imps.icici.entity.AdminEntity;

@Component
public class AdminMapper {

	ObjectMapper mapper = new ObjectMapper();
	
	public AdminRegDTO enityToDto(AdminEntity oAdminEntity) {
		return mapper.convertValue(oAdminEntity, AdminRegDTO.class);
	}
	
	public AdminEntity dtoToEntity(AdminRegDTO oAdminDTO) {
		return mapper.convertValue(oAdminDTO, AdminEntity.class);
	}
}
