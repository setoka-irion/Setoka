package com.practice.setoka.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.practice.setoka.Encryption;
import com.practice.setoka.Redirect;
import com.practice.setoka.Enum.Status;
import com.practice.setoka.dto.UsersDto;
import com.practice.setoka.service.EmailService;
import com.practice.setoka.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class LoginController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	
	
	@GetMapping(value = "Login")
	public String login(@RequestParam(value = "expired", required = false) String expired, Model model
			) {
		if(expired!=null) {
			model.addAttribute("error", "다른 위치에서 로그인되어 세션이 종료되었습니다.");
		}
		return "User/Login";
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
	public String SingupSubmit(Model model,@Valid UsersDto dto, @RequestParam("passwordCom") String passwordCom, HttpSession session) {
		
		if(session.getAttribute("emailVerifyCode") == null)
		{
			model.addAttribute("errorMessage", "이메일 인증 실패");
			model.addAttribute("Users", dto);
			return Redirect.SignUp;
		}
		
		//비밀번호 검증
		if(!userService.passwordInvalid(dto.getPassword()))
		{
			//회원가입 실패
			//입력값 다시 채워줘야 함
			model.addAttribute("errorMessage", "대소문자 특수문자 포함 8자 이상");
			model.addAttribute("Users", dto);
			return Redirect.SignUp;
		}
		
		if(!dto.getPassword().equals(passwordCom)) 
		{
			model.addAttribute("errorMessage", "비밀번호 확인이 일치하지 않음");
			model.addAttribute("Users", dto);
			return Redirect.SignUp;
		}

		//아이디 중복 검사 (삭제된 경우는 없는걸로 침) user가 없거나 있어도 삭제면 false가 리턴된다는 뜻
		if(userService.existsByName(dto.getId()))
		{
			//회원가입 실패
			//입력값 다시 채워줘야 함
			model.addAttribute("errorMessage", "중복 아이디");
			model.addAttribute("Users", dto);
			return Redirect.SignUp;
		}
		
		//닉네임 체크
		if(dto.getNickName().isEmpty())
		{
			//회원가입 실패
			model.addAttribute("errorMessage", "닉네임이 비어있음");
			model.addAttribute("Users", dto);
			return Redirect.SignUp;
		}
		
		if(dto.getRealName().isEmpty())
		{
			//회원가입 실패
			model.addAttribute("errorMessage", "이름이 비어있음");
			model.addAttribute("Users", dto);
			return Redirect.SignUp;
		}
		
		if(dto.getPhoneNumber().isEmpty())
		{
			//회원가입 실패
			model.addAttribute("errorMessage", "전화번호가 비어있음");
			model.addAttribute("Users", dto);
			return Redirect.SignUp;
		}
		
		// 닉네임 중복 검사 (삭제된 경우는 없는걸로 침) user가 없거나 있어도 삭제면 false가 리턴된다는 뜻
		if (userService.selectByNickName(dto.getNickName()) != null) {
			// 회원가입 실패
			// 입력값 다시 채워줘야 함
			model.addAttribute("errorMessage", "중복 닉네임");
			model.addAttribute("Users", dto);
			return Redirect.SignUp;
		}

		//전화번호 숫자만 남기기
		dto.setPhoneNumber(dto.getPhoneNumber().replaceAll("-", "")); 
		

		// 암호화
		dto.setPassword(Encryption.Encoder(dto.getPassword()));
		dto.setStatus(Status.정상);
		
		// insert 사실상 항상 true 아닌가
		if (!userService.insertUserNomal(dto)) 
		{
			//회원가입 실패
			model.addAttribute("errorMessage", "알수없는 오류");
			return Redirect.SignUp;
		}

		//회원가입 성공시 인증한 이메일 세션에서 없애기
		session.removeAttribute("emailVerifyCode");
		// 회원가입 성공
		return Redirect.home;
	}
	
	@GetMapping(value = "/passwordFind")
	public String passwordFindForm(Model model)
	{
		return "User/passwordFind";
	}

	@PostMapping(value = "/sendPasswordFind")
	public String sendPasswordFind(@RequestParam("email") String email, Model model)
	{
		if(userService.selectByID(email) == null)
		{
			//없는 메일인 경우
			model.addAttribute("sendMessage", "없는 메일");
		}
		else
		{
			//메일 보내기
			if(emailService.SendPasswordResetMessage(email))
				//메세지를 보냈다는 메세지
				model.addAttribute("sendMessage", "메일로 링크를 보냄");
			else
				model.addAttribute("sendMessage", "알수없는 오류");
		}
		
		model.addAttribute("email", email);
		//다시 폼으로 보내기
		return "User/passwordFind";
	}
	
	
	
	
	
	
	@PostMapping("/sendSingUpCode")
	public ResponseEntity<String> sendSingUpCode(@RequestBody Map<String, String> request)
	{
		String email = request.get("email");
		
		//아이디 중복 검사
		if(userService.existsByName(email))
		{
			return ResponseEntity.badRequest().body("중복된 이메일");
		}
		
		//db 저장
		int code = emailService.GetCode();
		emailService.SendMessageVerifyCode(code, email);
//		if()
//		{
//			return ResponseEntity.ok("인증번호 전송");
//		}
		return ResponseEntity.ok("인증번호 전송");// ResponseEntity.badRequest().body("인증번호 전송 실패");
	}
	
	@PostMapping("/verifySingUpCode")
	public ResponseEntity<String> verifySingUpCode(@RequestBody Map<String, String> request, HttpSession session)
	{
		String email = request.get("email");
		int verifyCode = Integer.parseInt(request.get("code"));

		if(emailService.VerifyCodeEqule(email, verifyCode))
		{
			session.setAttribute("emailVerifyCode", email);
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
