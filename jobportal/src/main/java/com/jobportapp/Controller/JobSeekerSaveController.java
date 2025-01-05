package com.jobportapp.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jobportapp.entity.JobPostActivity;
import com.jobportapp.entity.JobSeekerProfile;
import com.jobportapp.entity.JobSeekerSave;
import com.jobportapp.entity.Users;
import com.jobportapp.services.JobPostActivityService;
import com.jobportapp.services.JobSeekerProfileService;
import com.jobportapp.services.JobSeekerSaveService;
import com.jobportapp.services.UsersService;
import com.jobportapp.util.FileDownloadUtil;

@Controller
public class JobSeekerSaveController {

	private final UsersService usersService;
	private final JobSeekerProfileService jobSeekerProfileService;
	private final JobPostActivityService jobPostActivityService;
	private final JobSeekerSaveService jobSeekerSaveService;

	@Autowired
	public JobSeekerSaveController(UsersService usersService, JobSeekerProfileService jobSeekerProfileService,
			JobPostActivityService jobPostActivityService, JobSeekerSaveService jobSeekerSaveService) {
		this.usersService = usersService;
		this.jobSeekerProfileService = jobSeekerProfileService;
		this.jobPostActivityService = jobPostActivityService;
		this.jobSeekerSaveService = jobSeekerSaveService;
	}

	
	@PostMapping("job-details/save/{id}")
	public String save(@PathVariable("id") int id, JobSeekerSave jobSeekerSave) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUsername = authentication.getName();
			Users user = usersService.findByEmail(currentUsername);
			Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getOne(user.getUserId());
			JobPostActivity jobPostActivity = jobPostActivityService.getOne(id);
			if (seekerProfile.isPresent() && jobPostActivity != null) {
				jobSeekerSave.setJob(jobPostActivity);
				jobSeekerSave.setUserId(seekerProfile.get());
			} else {
				throw new RuntimeException("User not found");
			}
			jobSeekerSaveService.addNew(jobSeekerSave);
		}
		return "redirect:/dashboard/";
	}

	@GetMapping("saved-jobs/")
	public String savedJobs(Model model) {

		List<JobPostActivity> jobPost = new ArrayList<>();
		Object currentUserProfile = usersService.getCurrentUserProfile();

		List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService
				.getCandidatesJob((JobSeekerProfile) currentUserProfile);
		for (JobSeekerSave jobSeekerSave : jobSeekerSaveList) {
			jobPost.add(jobSeekerSave.getJob());
		}
		model.addAttribute("jobPost", jobPost);
		model.addAttribute("user", currentUserProfile);

		return "saved-jobs";
	}

	
	@GetMapping("job-seeker-profile/{id}")
	public String candidateProfile(@PathVariable("id") int id, Model model) {

		Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getOne(id);
		model.addAttribute("profile", seekerProfile.get());
		return "job-seeker-profile";
	}

	
	@GetMapping("job-seeker-profile/downloadResume")
	public ResponseEntity<?> downloadResume(@RequestParam(value = "fileName") String fileName,
			@RequestParam(value = "userID") String userId) {

		FileDownloadUtil downloadUtil = new FileDownloadUtil();
		Resource resource = null;

		try {
			resource = downloadUtil.getFileAsResourse("photos/candidate/" + userId, fileName);
		} catch (IOException e) {
			return ResponseEntity.badRequest().build();
		}

		if (resource == null) {
			return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
		}

		String contentType = "application/octet-stream";
		String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(resource);

	}
}
