package com.jobportapp.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**Once logged in successfully: 
 * retrieve the user object, check the roles of the user
 * if job seeker or recruiter role then send them to Dashboard page
 * */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username= userDetails.getUsername();
		System.out.println("The username " + username+ " is logged in");
		boolean hasJobSeekerRole = authentication.getAuthorities().stream().anyMatch(r -> r
				.getAuthority().equals("Job Seeker"));
		boolean hasRecuriterRole = authentication.getAuthorities().stream().anyMatch(r -> r
				.getAuthority().equals("Recruiter"));
		if(hasRecuriterRole|| hasJobSeekerRole) {
			response.sendRedirect("/dashboard/");
		}
		

	}

}
