package com.imps.icici.mapper;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imps.icici.dto.UserDto;
import com.imps.icici.entity.UserEntity;

@Component
public class CustomeMapper {

	private ObjectMapper mapper = new ObjectMapper();
	
	public UserDto userDtoToEntity(UserEntity oUserEntity) {
		return mapper.convertValue(oUserEntity, UserDto.class);
	}
}
