package com.practice.setoka;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.practice.setoka.dto.UsersDto;

import jakarta.servlet.http.HttpSession;

//jin
@Controller
public class LoginController 
{
	//로그인
	@GetMapping(value = "Login")
	public String login()
	{
		return "Login";
	}
	
	@PostMapping(value = "LoginSubmit")
	public String loginSubmit(HttpSession session, UsersDto dto)
	{
		//중복처리
		session.setAttribute("LoginSession", session);
		return Redirect.home;
	}
}
