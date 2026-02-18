package com.imps.icici.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.function.SingletonSupplier;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users_tbl")
public class UserEntity implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;
	private String username;
	private String password;
	private String email;
	private boolean isActive = false;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles",
			joinColumns = @JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name="role_id")
			)
	private Set<RoleEntity> roles = new HashSet<>();
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private UserOtpEntity otp;
	
	@OneToOne(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private UserAddressEntity address;
	
	@OneToOne(mappedBy = "user_account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private UserAccountDetails account;
	
	private LocalDateTime cretedAt = LocalDateTime.now();
	private LocalDateTime updateAt;
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
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
	public Set<RoleEntity> getRoles() {
		return roles;
	}
	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
	}
	public LocalDateTime getCretedAt() {
		return cretedAt;
	}
	public void setCretedAt(LocalDateTime cretedAt) {
		this.cretedAt = cretedAt;
	}
	public LocalDateTime getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(LocalDateTime updateAt) {
		this.updateAt = updateAt;
	}
	
	
	public UserOtpEntity getOtp() {
		return otp;
	}
	public void setOtp(UserOtpEntity otp) {
		this.otp = otp;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public UserAddressEntity getAddress() {
		return address;
	}
	public void setAddress(UserAddressEntity address) {
		this.address = address;
	}
	public UserAccountDetails getAccount() {
		return account;
	}
	public void setAccount(UserAccountDetails account) {
		this.account = account;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		String roles = getRole(getRoles());
		return Collections.singleton(new SimpleGrantedAuthority(roles.startsWith("ROLE_") ? roles :"ROLE_" + roles));
	}
	
	private static String getRole(Set<RoleEntity> roleEntity) {
		String roleName = null;
		for(RoleEntity roles : roleEntity) {
			roleName = roles.getRoleName();
		}
		return roleName;
	}
	
	
}
