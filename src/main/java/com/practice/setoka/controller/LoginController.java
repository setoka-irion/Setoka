package com.practice.setoka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.practice.setoka.Encryption;
import com.practice.setoka.Redirect;
import com.practice.setoka.dao.Users;
import com.practice.setoka.dto.UsersDto;
import com.practice.setoka.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	@Autowired
	private UserService userService;

	// 로그인 페이지로
	@GetMapping(value = "Login")
	public String login(Model model) {
		UsersDto user = new UsersDto();
		model.addAttribute("Users", user);

		return "Login";
	}

	// 로그인 확인
	@PostMapping(value = "Login")
	public String loginSubmit(HttpSession session, UsersDto dto) {
		// 중복처리

		// 복호화
		Users user = userService.SelectByID(dto.getId());
		if(Encryption.Decoder(user.getPassword(), dto.getPassword()))
		{
			// 로그인 성공
			// 세션처리
			session.setAttribute(Redirect.loginSession, user);
		}
					
		return Redirect.home;
	}

	@GetMapping(value = "Logout")
	public String logout(HttpSession session) {
		// 세션 없애기
		session.removeAttribute(Redirect.loginSession);
		return "";
	}

	// 회원가입 페이지로
	@GetMapping(value = "SignUp")
	public String Signup(Model model) {
		UsersDto user = new UsersDto();
		model.addAttribute("Users", user);

		return "SignUp";
	}

	// 회원가입 완료
	@PostMapping(value = "SingUpSubmit")
	public String SingupSubmit(HttpSession session, UsersDto dto) {
		// 중복처리
		
		// 암호화
		dto.setPassword(Encryption.Encoder(dto.getPassword()));

		// insert
		if (userService.InsertUserNomal(dto)) {
			// 로그인 성공
			// 세션처리
//			Users user = userService.SelectByID(dto.getId());
//			session.setAttribute("LoginSession", user);
		}

		return Redirect.home;
	}
}
