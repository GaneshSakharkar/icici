package com.imps.icici.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imps.icici.entity.RoleEntity;
import com.imps.icici.entity.UserEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long>{

	Optional<RoleEntity> findByRoleName(String string);

	boolean existsByRoleName(String string);

}
