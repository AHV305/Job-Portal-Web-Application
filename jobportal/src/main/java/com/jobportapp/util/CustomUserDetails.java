package com.jobportapp.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jobportapp.entity.Users;
import com.jobportapp.entity.UsersType;

public class CustomUserDetails implements UserDetails {

	private final Users user;

	// Constructor
	public CustomUserDetails(Users user) {
		super();
		this.user = user;
	}

	//Return the roles for this user
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		UsersType usersType = user.getUserTypeId();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(usersType.getUserTypeName()));
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	public boolean isCredentialNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
