package com.practice.setoka.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.practice.setoka.Redirect;
import com.practice.setoka.dto.UsersDto;

import jakarta.servlet.http.HttpSession;

//jin
@Controller
public class LoginController 
{
	//로그인 페이지로
	@GetMapping(value = "Login")
	public String login()
	{
		return "Login";
	}
	
	//로그인 확인
	@PostMapping(value = "LoginSubmit")
	public String loginSubmit(HttpSession session, UsersDto dto)
	{
		//중복처리
		//세션처리
		session.setAttribute("LoginSession", session);
		return Redirect.home;
	}
	
	@GetMapping(value = "Logout")
	public String logout()
	{
		//세션 없애기
		return "";
	}
	
	//회원가입 페이지로
	@GetMapping(value = "Sign up")
	public String Signup()
	{
		return "";
	}
	
	//회원가입 완료
	@PostMapping(value = "Sing up Submit")
	public String SingupSubmit()
	{
		return "";
	}
}
