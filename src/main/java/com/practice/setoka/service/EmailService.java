package com.practice.setoka.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.practice.setoka.dao.PasswordReset;
import com.practice.setoka.dao.VerifyCode;
import com.practice.setoka.mapper.MailMapper;

@Service
public class EmailService
{
	@Autowired
	private MailMapper mailMapper;
	
	private final JavaMailSender mailSender;
	
	public EmailService(JavaMailSender javaMailSender)
	{
		this.mailSender = javaMailSender;
	}
	
	@Async
	//메일을 보내주는 메소드
	public void SendSimpleMessage(String to, String subject, String context)
	{
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(context);
		mailSender.send(message);
	}
	
	//랜덤으로 코드를 만들어주는 메소드
	public int GetCode()
	{
		Random random = new Random();
		return random.nextInt(900000) + 100000;
	}
	
	//num 값으로 리턴해줌
	public VerifyCode selectByNum(int num)
	{
		return mailMapper.selectVerifyCode(num);
	}
	
	//email로 가장 num이 큰걸로 리턴해주지만 이론상 한 email에 하나의 데이터만 존재함
	public VerifyCode selectByEmail(String email)
	{
		return mailMapper.selectVerifyCodeByID(email);
	}
	
	//email로 가장 num이 큰걸로 5분 이내의 데이터만 리턴해줌
	public VerifyCode selectByEmailLimit(String email)
	{
		return mailMapper.selectVerifyCodeByIDLimit(email);
	}
	
	//이메일 인증 코드를 db에 넣고 메일을 보내줌
	public boolean SendMessageVerifyCode(int code, String email)
	{
		//이미 있는 메일인지 체크
		VerifyCode verifyCode = selectByEmail(email);
		//없는 메일이면 
		if(verifyCode == null)
			//새로 추가
			if(!mailMapper.insertCode(email, code))
				return false;
		//있는 경우
		else
			//코드와 시기를 수정함
			if(!mailMapper.updateVerifyCode(email, code))
				return false;
		
		//메일 보내기
		SendSimpleMessage(email, "인증번호", "인증번호 : " + code);
		return true;
	}
	
	//코드가 db에 있는 데이터와 같은지 대조해서 리턴해줌
	public boolean VerifyCodeEqule(String email, int code)
	{
		//5분이내에 메일로 보낸 코드가 있는지 확인
		VerifyCode verify = selectByEmailLimit(email);
		//있다면
		if(verify != null)
		{
			int verifycode = verify.getVerifyCode();
			//코드를 비교해봄
			if(code == verifycode)
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	//비밀번호 재설정을 db에 저장하고 메일을 보내줌
	public boolean SendPasswordResetMessage(String email)
	{
		//db에 저장을 하고
		PasswordReset reset = mailMapper.selectPasswordReset(email);
		//email이 없었다면
		if(reset == null)
			//추가
			if(!mailMapper.insertPasswordReset(email))
				return false;
		//email이 있었다면
		else
			//수정
			if(!mailMapper.updatePasswordReset(email))
				return false;
				
		// 메일을 보내고
		SendSimpleMessage(email, "비밀번호 재설정", "주소");
		
		return true;
	}
	
	//비밀번호 재설정링크가 유효한지 리턴해줌
	public boolean PasswordResetValidity(String email)
	{
		PasswordReset reset = mailMapper.selectPasswordResetLimit(email);
		if(reset == null)
			return false;
		
		return true;
	}
}
