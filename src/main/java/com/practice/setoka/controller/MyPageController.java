package com.practice.setoka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.practice.setoka.Encryption;
import com.practice.setoka.Redirect;
import com.practice.setoka.Enum.Status;
import com.practice.setoka.dao.Users;
import com.practice.setoka.dto.UsersDto;
import com.practice.setoka.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MyPageController {

	@Autowired
	private UserService userService;

	// 마이페이지
	@GetMapping(value = "MyPage")
	public String myPage(HttpSession session) {
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		if (user == null) {
			SessionUrlHandler.save(session, "MyPage");
			return Redirect.LoginForm;
		}
		return "MyPage";
	}

	// 비밀번호 재확인
	@GetMapping(value = "PasswordConfirm")
	public String passwordConfirmForm(HttpSession session) {
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		if (user == null) {
			SessionUrlHandler.save(session, "MyPage");
			return Redirect.LoginForm;
		}
		return "PasswordConfirm";
	}

	@PostMapping(value = "PasswordConfirm")
	public String passwordConfirmSubmit(@RequestParam("password") String password, HttpSession session) {
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		if (Encryption.Decoder(user.getPassword(), password)) {
			String url = SessionUrlHandler.load(session);
			session.setAttribute("PasswordConfirmed", "PasswordConfirmed");
			return url;
		}
		return Redirect.passwordConfirm;
	}

	// 개인정보수정
	@GetMapping(value = "ModifyUser")
	public String modifyUser(HttpSession session, Model model) {
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		if (user == null) {
			return Redirect.home;
		}

		String passwordConfirm = (String) session.getAttribute("PasswordConfirmed");
		if (passwordConfirm == null) {
			SessionUrlHandler.save(session, "ModifyUser");
			return Redirect.passwordConfirm;
		}
		session.removeAttribute("PasswordConfirmed");

		UsersDto dto = new UsersDto(user);
		// html을 꾸며줄 클래스 넣기
		model.addAttribute("UsersDto", dto);
		// 수정 페이지로 이동
		return "ModifyUser";
	}

	@PostMapping(value = "ModifyUser")
	public String modifyUserPost(HttpSession session, Model model, UsersDto userDto) {
		// 로그인 되어 있는 사람의 정보
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		// 수정될 정보
		if (userService.updateUserDto(userDto)) {
			// 정보 수정 성공
			session.removeAttribute(Redirect.loginSession);
			user = userService.selectByID(userDto.getId());
			session.setAttribute(Redirect.loginSession, user);
		} else {
			// 정보 수정 실패
		}
		// 정보 수정
		return "MyPage";
	}

	// 비밀번호 변경
	@GetMapping(value = "ChangePassword")
	public String changePassword(HttpSession session) {
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		if (user == null) {
			return Redirect.home;
		}
		
		String passwordConfirm = (String) session.getAttribute("PasswordConfirmed");
		if (passwordConfirm == null) {
			SessionUrlHandler.save(session, "ChangePassword");
			return Redirect.passwordConfirm;
		}
		session.removeAttribute("PasswordConfirmed");
		
		return "ChangePassword";
	}
	
	@PostMapping(value = "ChangePassword")
	public String changePasswordsubmit(@RequestParam("password") String password, @RequestParam("passwordCon") String passwordCon, HttpSession session) {
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		String encordPassowrd = Encryption.Encoder(password);
		if (Encryption.Decoder(encordPassowrd, passwordCon) && !Encryption.Decoder(user.getPassword(), passwordCon)) {
			UsersDto dto = new UsersDto(user);
			dto.setPassword(encordPassowrd);
			userService.updateUserDto(dto);
			session.removeAttribute(Redirect.loginSession);
			return Redirect.home;
		}
		return Redirect.changePassword;
	}
	
	// 동물프로필
	@GetMapping(value = "AnimalProfile")
	public String animalProfile() {
		return "AnimalProfile";
	}

	// 프로필 추가
	@GetMapping(value = "AddProfile")
	public String addProfile() {
		return "AddProfile";
	}

	// 다마
	@GetMapping(value = "Damagochi")
	public String damagochi() {
		return "Damagochi";
	}

	// 탈퇴
	@GetMapping(value = "Withdrawal")
	public String withdrawal(HttpSession session) {
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		if (user == null) {
			return Redirect.home;
		}
		return "Withdrawal";
	}
	
	@PostMapping(value = "Withdrawal")
	public String withdrawalSubmit(@RequestParam("password") String password, HttpSession session) {
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		if(Encryption.Decoder(user.getPassword(), password)) {
			UsersDto dto = new UsersDto(user);
			dto.setStatus(Status.삭제);
			userService.updateUserDto(dto);
			session.removeAttribute(Redirect.loginSession);
			return Redirect.home;
		}
		return Redirect.withdrawal;
	}
}
