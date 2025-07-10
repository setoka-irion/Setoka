package com.practice.setoka.service;

import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService
{
	private final JavaMailSender mailSender;
	private final String text = "인증번호 : ";
	
	public EmailService(JavaMailSender javaMailSender)
	{
		this.mailSender = javaMailSender;
	}
	
	public void SendSimpleMessage(String to, String subject)
	{
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		String context = text + GetCode();
		message.setText(context);
		mailSender.send(message);
	}
	
	private int GetCode()
	{
		Random random = new Random();
		return random.nextInt(900000) + 100000;
	}
}
