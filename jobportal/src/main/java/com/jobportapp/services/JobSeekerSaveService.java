package com.jobportapp.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jobportapp.entity.JobPostActivity;
import com.jobportapp.entity.JobSeekerProfile;
import com.jobportapp.entity.JobSeekerSave;
import com.jobportapp.repository.JobSeekerSaveRepository;

@Service
public class JobSeekerSaveService {
	
	private final JobSeekerSaveRepository jobSeekerSaveRepository;

    public JobSeekerSaveService(JobSeekerSaveRepository jobSeekerSaveRepository) {
        this.jobSeekerSaveRepository = jobSeekerSaveRepository;
    }

    public List<JobSeekerSave> getCandidatesJob(JobSeekerProfile userAccountId) {
        return jobSeekerSaveRepository.findByUserId(userAccountId);
    }

    public List<JobSeekerSave> getJobCandidates(JobPostActivity job) {
        return jobSeekerSaveRepository.findByJob(job);
    }

    public void addNew(JobSeekerSave jobSeekerSave) {
        jobSeekerSaveRepository.save(jobSeekerSave);
    }

}
