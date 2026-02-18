package com.imps.icici.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.imps.icici.entity.IfscBranchCode;

@Repository
public interface IfscRepository extends JpaRepository<IfscBranchCode, Long> {

	Optional<IfscBranchCode> findByPincode(long pincode);
	
//	@Query(value = "insert into icici.ifsc_branch_code(branchName,ifsc,pincode) value(?,?,?)",nativeQuery = true)
//	boolean insertRow(String branchName,String ifsc, long pincode);

}
