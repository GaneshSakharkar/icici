package com.imps.icici.emailservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

	@Value("${spring.mail.username}")
	private String from;
	
	@Autowired
	private JavaMailSender oJavaMailSender;
	
	public boolean emailSend(String subject, String to, String text) {
		
		boolean isEmailSend = false;
		
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(from);
			message.setTo(to);
			message.setSubject(subject);
			message.setText(text);
			oJavaMailSender.send(message);
			isEmailSend = true;	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isEmailSend;
		
	}
}
