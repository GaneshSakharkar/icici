package com.imps.icici.dto;

import jakarta.validation.constraints.NotBlank;


public class AdminRegDTO {

private long adminId;
	
	//@NotBlank(message="First Name should not black or empty.")
	private String firstName;
	
	//@NotBlank(message="Last Name should not black or empty.")
	private String lastName;
	
	@NotBlank(message="username should not black or empty.")
	private String username;
	
	@NotBlank(message="password should not black or empty.")
	private String password;
	
	@NotBlank(message="email should not black or empty.")
	private String email;
	
	public long getAdminId() {
		return adminId;
	}
	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
