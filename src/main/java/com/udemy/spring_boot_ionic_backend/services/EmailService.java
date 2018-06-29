package com.udemy.spring_boot_ionic_backend.services;

import org.springframework.mail.SimpleMailMessage;
import com.udemy.spring_boot_ionic_backend.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	void sendEmail(SimpleMailMessage msg);
	
}