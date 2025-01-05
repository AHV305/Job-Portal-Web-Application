package com.jobportapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.jobportapp.services.CustomUserDetailsService;

@Configuration
public class webSecurityConfig {

	private CustomUserDetailsService customUserDetialsService;
	private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

	@Autowired
	public webSecurityConfig(CustomUserDetailsService customUserDetialsService,
			CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
		super();
		this.customUserDetialsService = customUserDetialsService;
		this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
	}

	private final String[] publicUrl = { "/", "/global-search/**", "/register", "/register/**", "/webjars/**",
			"/resources/**", "/assets/**", "/css/**", "/summernote/**", "/js/**", "/*.css", "/*.js", "/*.js.map",
			"/fonts**", "/favicon.ico", "/resources/**", "/error" };

	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authenticationProvider(authenticationProvider());
		http.authorizeHttpRequests(auth -> {
			auth.requestMatchers(publicUrl).permitAll();
			auth.anyRequest().authenticated();
		});

		http.formLogin(form -> form.loginPage("/login").permitAll().successHandler(customAuthenticationSuccessHandler))
				.logout(logout -> {
					logout.logoutUrl("/logout");
					logout.logoutSuccessUrl("/");
				}).cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable());
		return http.build();

	}

	/**
	 * our custom authentication provider- Tell spring security how to find our
	 * users and how to authenticate passwords
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		// Tell spring security how to retrieve the users from the database
		authenticationProvider.setUserDetailsService(customUserDetialsService);
		return authenticationProvider;
	}

	/**
	 * Our Custom Password Encoder Tell Spring Security how to authenticate
	 * passwords (plain text or encryption)
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
