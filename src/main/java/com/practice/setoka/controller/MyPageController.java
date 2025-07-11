package com.practice.setoka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.practice.setoka.Encryption;
import com.practice.setoka.Redirect;
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
	
	
	//비밀번호 재확인
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
	public String passwordConfirmSubmit(@RequestParam("password")String password, HttpSession session) {
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		if(Encryption.Decoder(user.getPassword(), password))
		{
			session.setAttribute(Redirect.loginSession, user);
		}
		return "PasswordConfirm";
	}
	
	
	// 개인정보수정
	@GetMapping(value = "ModifyUser")
	public String modifyUser(HttpSession session, Model model) {
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		if (user == null) {
			return Redirect.LoginForm;
		}
		UsersDto dto = new UsersDto();
		dto.setId(user.getId());
		dto.setPassword(user.getPassword());
		dto.setNickName(user.getNickName());
		dto.setRealName(user.getRealName());
		dto.setPhoneNumber(user.getPhoneNumber());
		// html을 꾸며줄 클래스 넣기
		model.addAttribute("UsersDto", dto);
		// 수정 페이지로 이동
		return "ModifyUser";
	}
	
	@PostMapping(value = "ModifyUser")
	public String modifyUserPost(HttpSession session, Model model, UsersDto userDto) {
		// 로그인 되어 있는 사람의 정보
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		// 로그인 세션이 없는 상태
		if (user == null) {
			return Redirect.home;
		}
		System.out.println(userDto.getRealName());
		System.out.println(userDto.getId());
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
	public String changePassword(HttpSession session, Model model, UsersDto userDto) {
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		if (user == null) {
			SessionUrlHandler.save(session, "ChangePassword");
			return Redirect.LoginForm;
		}
		return "ChangePassword";
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
	public String withdrawal() {
		return "Withdrawal";
	}
}
