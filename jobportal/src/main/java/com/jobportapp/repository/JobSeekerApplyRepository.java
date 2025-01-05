package com.jobportapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobportapp.entity.JobPostActivity;
import com.jobportapp.entity.JobSeekerApply;
import com.jobportapp.entity.JobSeekerProfile;

@Repository
public interface JobSeekerApplyRepository extends JpaRepository<JobSeekerApply, Integer>{

	List<JobSeekerApply> findByUserId(JobSeekerProfile userId);

    List<JobSeekerApply> findByJob(JobPostActivity job);
}
