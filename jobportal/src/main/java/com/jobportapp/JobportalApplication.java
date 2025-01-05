package com.jobportapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
@SpringBootApplication

public class JobportalApplication {
	private static final Logger logger=LoggerFactory.getLogger(JobportalApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(JobportalApplication.class, args);
		 
		logger.info("Appllication entered in to main class");
	}	

}
