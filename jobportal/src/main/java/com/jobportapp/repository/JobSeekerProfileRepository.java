package com.jobportapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobportapp.entity.JobSeekerProfile;

public interface JobSeekerProfileRepository extends JpaRepository<JobSeekerProfile, Integer> {

}
