package com.imps.icici.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_otp_tbl")
public class UserOtpEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long otp_id;
	private String otp;
	private boolean isEmailVerify = false;
	private LocalDateTime createdAt = LocalDateTime.now();
	private LocalDateTime expityTime = LocalDateTime.now().plusMinutes(10);
	
	@OneToOne
	@JoinColumn(name = "userId")
	private UserEntity user;

	public long getOtp_id() {
		return otp_id;
	}

	public void setOtp_id(long otp_id) {
		this.otp_id = otp_id;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public boolean isEmailVerify() {
		return isEmailVerify;
	}

	public void setEmailVerify(boolean isEmailVerify) {
		this.isEmailVerify = isEmailVerify;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getExpityTime() {
		return expityTime;
	}

	public void setExpityTime(LocalDateTime expityTime) {
		this.expityTime = expityTime;
	}
	
	
	
}
