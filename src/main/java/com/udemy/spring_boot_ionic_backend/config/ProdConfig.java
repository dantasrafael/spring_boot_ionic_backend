package com.udemy.spring_boot_ionic_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.udemy.spring_boot_ionic_backend.services.EmailService;
import com.udemy.spring_boot_ionic_backend.services.SmtpEmailService;

@Configuration
@Profile("prod")
public class ProdConfig {
	
	@Bean
	public EmailService emailService( ) {
		return new SmtpEmailService();
	}
	
}
