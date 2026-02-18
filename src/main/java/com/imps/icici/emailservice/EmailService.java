package com.imps.icici.emailservice;

public interface EmailService {

	public boolean emailSend(String subject, String to, String text);
}
