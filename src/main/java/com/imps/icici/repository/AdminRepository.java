package com.imps.icici.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imps.icici.entity.AdminEntity;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long>{

	Optional<AdminEntity> findByEmail(String email);

	Optional<AdminEntity> findByUsername(String username);

}
