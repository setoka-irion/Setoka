package com.practice.setoka.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.practice.setoka.dao.VerifyCode;
import com.practice.setoka.mapper.MailMapper;

@Service
public class EmailService
{
	@Autowired
	private MailMapper mailMapper;
	
	private final JavaMailSender mailSender;
	private final String text = "인증번호 : ";
	
	public EmailService(JavaMailSender javaMailSender)
	{
		this.mailSender = javaMailSender;
	}
	
	public void SendSimpleMessage(String to, String subject, String context)
	{
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		context = text + context;
		message.setText(context);
		mailSender.send(message);
	}
	
	public int GetCode()
	{
		Random random = new Random();
		return random.nextInt(900000) + 100000;
	}
	
	public VerifyCode selectByNum(int num)
	{
		return mailMapper.selectVerifyCode(num);
	}
	
	public VerifyCode selectByEmail(String email)
	{
		return mailMapper.selectVerifyCodeByID(email);
	}
	
	public boolean insertCode(int code, String email)
	{
		return mailMapper.insertCode(email, code);
	}
}
