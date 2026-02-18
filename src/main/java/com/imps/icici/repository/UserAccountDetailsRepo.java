package com.imps.icici.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imps.icici.entity.UserAccountDetails;

@Repository
public interface UserAccountDetailsRepo extends JpaRepository<UserAccountDetails, Long>{

	Optional<UserAccountDetails> findByAccountNumber(String accountNumber);

}
