package com.jobportapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jobportapp.entity.Users;
import com.jobportapp.repository.UsersRepository;
import com.jobportapp.util.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UsersRepository usersRepository;

	@Autowired
	public CustomUserDetailsService(UsersRepository usersRepository) {
		super();
		this.usersRepository = usersRepository;
	}

	
	// Tell Spring Security how to retrieve the users form the database
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = usersRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Could not found user"));
		return new CustomUserDetails(user);
	}

}
