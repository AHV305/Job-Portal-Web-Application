package com.jobportapp.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jobportapp.entity.JobSeekerProfile;
import com.jobportapp.entity.Users;
import com.jobportapp.repository.JobSeekerProfileRepository;
import com.jobportapp.repository.UsersRepository;

@Service
public class JobSeekerProfileService {

	private final JobSeekerProfileRepository jobSeekerProfileRepository;
	private final UsersRepository usersRepository;

	@Autowired
	public JobSeekerProfileService(JobSeekerProfileRepository jobSeekerProfileRepository,
			UsersRepository usersRepository) {
		super();
		this.jobSeekerProfileRepository = jobSeekerProfileRepository;
		this.usersRepository = usersRepository;
	}

	public Optional<JobSeekerProfile>getOne(Integer id){
		return jobSeekerProfileRepository.findById(id);
	}

	public JobSeekerProfile addNew(JobSeekerProfile jobSeekerProfile) {
		return jobSeekerProfileRepository.save(jobSeekerProfile);
	}

	public JobSeekerProfile getCurrentSeekerProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users users = usersRepository.findByEmail(currentUsername).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            Optional<JobSeekerProfile> seekerProfile = getOne(users.getUserId());
            return seekerProfile.orElse(null);
        } else return null;

    }
}
