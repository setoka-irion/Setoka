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
import org.springframework.web.multipart.MultipartFile;

import com.practice.setoka.Encryption;
import com.practice.setoka.Redirect;
import com.practice.setoka.Enum.Status;
import com.practice.setoka.dao.Users;
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
		
		//db에서 정보 가져오기
		Users user = userService.loginUser(dto);
		//등록된 정보 없음
		if(user == null)
		{
			//다시 로그인 폼으로
			return Redirect.LoginForm;
		}
		
		// 복호화, 같은 id는 있지만 비밀번호가 다를경우
		if(!Encryption.Decoder(user.getPassword(), dto.getPassword()))
		{
			//다시 로그인 폼으로
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
		

		//아이디 중복 검사 (삭제된 경우는 없는걸로 침) user가 없거나 있어도 삭제면 false가 리턴된다는 뜻
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
		dto.setStatus(Status.정상);
		
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
	
	@GetMapping(value = "/passwordFind")
	public String passwordFindForm(Model model)
	{
		return "passwordFind";
	}

	@PostMapping(value = "/sendPasswordFind")
	public String sendPasswordFind(@RequestParam("email") String email, HttpSession session, Model model)
	{
		System.out.println(email);
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
		return "passwordFind";
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

		if(emailService.SendMessageVerifyCode(code, email))
		{
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
	
	@GetMapping(value = "/FileTest")
	public String Test(HttpSession session)
	{
		Users user = (Users)session.getAttribute(Redirect.loginSession);
		if(user == null)
			return Redirect.home;
		
		return "/FileTest";
	}
	@PostMapping(value = "/FileTest")
	public String TTEst(@RequestParam("profile") MultipartFile[] files, HttpSession session)
	{
		Users user = (Users)session.getAttribute(Redirect.loginSession);
		if(user == null)
			return Redirect.home;
		
		if(files == null && files.length == 0)
		{
			System.out.println("파일이 없음");
		}
		else
		{
			System.out.println("파일의 이름 : " + files[0].getOriginalFilename());
		}
		
		userService.insertProfilephoto(files[0], new UsersDto(user));
		
		return "/FileTest";
	}
}
