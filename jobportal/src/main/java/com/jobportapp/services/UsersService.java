package com.jobportapp.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jobportapp.entity.JobSeekerProfile;
import com.jobportapp.entity.RecruiterProfile;
import com.jobportapp.entity.Users;
import com.jobportapp.repository.JobSeekerProfileRepository;
import com.jobportapp.repository.RecruiterProfileRepository;
import com.jobportapp.repository.UsersRepository;

@Service
public class UsersService {

	private final UsersRepository usersRepository;
	private final RecruiterProfileRepository recruiterProfileRepository;
	private final JobSeekerProfileRepository jobSeekerProfileRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UsersService(UsersRepository usersRepository, RecruiterProfileRepository recruiterProfileRepository,
			JobSeekerProfileRepository jobSeekerProfileRepository, PasswordEncoder passwordEncoder) {
		super();
		this.usersRepository = usersRepository;
		this.recruiterProfileRepository = recruiterProfileRepository;
		this.jobSeekerProfileRepository = jobSeekerProfileRepository;
		// Acutal encoder that we will inject is BCrypt password encoder that we set up
		// in the cogiguration file
		this.passwordEncoder = passwordEncoder;
	}

	public Users addNew(Users users) {
		users.setActive(true);
		users.setRegistrationDate(new Date(System.currentTimeMillis()));
		// During registration encrypt user password
		users.setPassword(passwordEncoder.encode(users.getPassword()));
		Users savedUser = usersRepository.save(users);
		int userTypeId = users.getUserTypeId().getUserTypeId();
		if (userTypeId == 1) {
			recruiterProfileRepository.save(new RecruiterProfile(savedUser));
		} else {
			jobSeekerProfileRepository.save(new JobSeekerProfile(savedUser));
		}
		return savedUser;
	}

	public Optional<Users> getUserByEmail(String email) {
		return usersRepository.findByEmail(email);
	}

	public Object getCurrentUserProfile() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String username = authentication.getName();
			Users users = usersRepository.findByEmail(username)
					.orElseThrow(() -> new UsernameNotFoundException("Could not found" + "user"));
			int userId = users.getUserId();
			if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
				RecruiterProfile recruiterProfile = recruiterProfileRepository.findById(userId)
						.orElse(new RecruiterProfile());
				return recruiterProfile;
			} else {
				JobSeekerProfile jobSeekerProfile = jobSeekerProfileRepository.findById(userId)
						.orElse(new JobSeekerProfile());
				return jobSeekerProfile;
			}
		}
		return null;
	}

	public Users getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			String username=authentication.getName();
			Users user = usersRepository.findByEmail(username)
					.orElseThrow(() -> new UsernameNotFoundException("Could not found" + "user"));
			return user;
		}
		return null;
	}

	public Users findByEmail(String currentUsername) {
        return usersRepository.findByEmail(currentUsername).orElseThrow(() -> new UsernameNotFoundException("User not " +
                "found"));
    }

	
}
