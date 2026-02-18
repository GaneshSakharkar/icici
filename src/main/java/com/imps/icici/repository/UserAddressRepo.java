package com.imps.icici.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imps.icici.entity.UserAddressEntity;

@Repository
public interface UserAddressRepo extends JpaRepository<UserAddressEntity, Long>{

	UserAddressEntity findByPincode(long pincode);

}
