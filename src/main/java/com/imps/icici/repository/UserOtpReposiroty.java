package com.imps.icici.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imps.icici.entity.UserOtpEntity;

@Repository
public interface UserOtpReposiroty extends JpaRepository<UserOtpEntity, Long>{

	Optional<UserOtpEntity> findByOtp(String userId);

}
