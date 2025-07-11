package com.practice.setoka.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.practice.setoka.Encryption;
import com.practice.setoka.Redirect;
import com.practice.setoka.dao.Users;
import com.practice.setoka.dao.VerifyCode;
import com.practice.setoka.dto.UsersDto;
import com.practice.setoka.service.EmailService;
import com.practice.setoka.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;

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

		Users user = userService.selectByID(dto.getId());
		if(user == null)
		{
			return Redirect.LoginForm;
		}
		
		// 복호화
		if(!Encryption.Decoder(user.getPassword(), dto.getPassword()))
		{
			return Redirect.LoginForm;
		}
		// 로그인 성공
		// 세션처리
		session.setAttribute(Redirect.loginSession, user);
		return SessionUrlHandler.load(session);
	}

	@GetMapping(value = "Logout")
	public String logout(HttpSession session) {
		// 세션 없애기
		session.removeAttribute(Redirect.loginSession);
		return Redirect.home;
	}

	// 회원가입 페이지로
	@GetMapping(value = "SignUp")
	public String Signup(Model model) {
		UsersDto user = new UsersDto();
		model.addAttribute("Users", user);
		return Redirect.SignUp;
	}

	// 회원가입 완료
	@PostMapping(value = "SignUp")
	public String SingupSubmit(HttpSession session, Model model, UsersDto dto) {
		//비밀번호 검증
		if(!userService.passwordInvalid(dto.getPassword()))
		{
			//회원가입 실패
			//입력값 다시 채워줘야 함
			model.addAttribute("errorMessage", "대소문자 특수문자 포함 8자 이상");
			model.addAttribute("Users", dto);
			return Redirect.SignUp;
		}
		

		//아이디 중복 검사
		if(userService.existsByName(dto.getId()))
		{
			//회원가입 실패
			//입력값 다시 채워줘야 함
			model.addAttribute("errorMessage", "중복 아이디");
			model.addAttribute("Users", dto);
			return Redirect.SignUp;
		}
		
		// 암호화
		dto.setPassword(Encryption.Encoder(dto.getPassword()));
		
		// insert 사실상 항상 true 아닌가
		if (!userService.insertUserNomal(dto)) 
		{
			//회원가입 실패
			model.addAttribute("errorMessage", "알수없는 오류");
			System.out.println("회원가입 실패");
			return Redirect.SignUp;
		}

		// 회원가입 성공
		return Redirect.home;
	}
	
	@GetMapping(value = "Cal")
	public String Test()
	{
		
		
		return "cal";
	}
	
	
	@PostMapping("/sendSingUpCode")
	public ResponseEntity<String> sendSingUpCode(@RequestBody Map<String, String> request)
	{
		String email = request.get("email");
		
		//db 저장
		int code = emailService.GetCode();

		if(emailService.SendMessageVerifyCode(code, email))
		{
			//emailService.SendSimpleMessage(email, "인증번호", code + "");
			
			return ResponseEntity.ok("인증번호 전송");
		}
		return ResponseEntity.badRequest().body("인증번호 전송 실패");
	}
	
	@PostMapping("/verifySingUpCode")
	public ResponseEntity<String> verifySingUpCode(@RequestBody Map<String, String> request)
	{
		String email = request.get("email");
		int verifyCode = Integer.parseInt(request.get("code"));
		if(emailService.VerifyCodeEqule(email, verifyCode))
		{
			return ResponseEntity.ok("인증 성공");
		}

		return ResponseEntity.badRequest().body("인증 실패");
	}
	
	public ResponseEntity<String> sendPasswordFind(@RequestBody Map<String, String> request)
	{
		String email = request.get("email");
		
		emailService.SendPasswordResetMessage(email);
		
		return ResponseEntity.ok("메일 전송 성공");
	}
}
