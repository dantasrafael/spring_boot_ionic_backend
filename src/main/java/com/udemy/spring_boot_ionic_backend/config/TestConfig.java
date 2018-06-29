package com.udemy.spring_boot_ionic_backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.udemy.spring_boot_ionic_backend.services.DBService;
import com.udemy.spring_boot_ionic_backend.services.EmailService;
import com.udemy.spring_boot_ionic_backend.services.MockEmailService;

@Configuration
@Profile("test")
public class TestConfig {
	
	@Autowired
	private DBService dbService;

	@Bean
	public boolean instantiateDatabase() throws Exception {
		dbService.instantiateTestDataBase();
		return true;
	}
	
	@Bean
	public EmailService emailService( ) {
		return new MockEmailService();
	}
	
}
