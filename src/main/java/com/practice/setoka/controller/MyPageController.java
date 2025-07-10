package com.practice.setoka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.practice.setoka.Redirect;
import com.practice.setoka.dao.Users;
import com.practice.setoka.dto.UsersDto;
import com.practice.setoka.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MyPageController 
{
	
	@Autowired
	private UserService userService;
	
	// 마이페이지
	@GetMapping(value = "MyPage")
	public String myPage(HttpSession session) 
	{
		Users user = (Users)session.getAttribute(Redirect.loginSession);
		if(user == null)
		{
			//로그인 되지 않은 상태
			//홈으로 리턴
			return Redirect.home;
		}
		
		return "MyPage";
	}

	// 개인정보수정
	@GetMapping(value = "ModifyUser")
	public String modifyUser(HttpSession session, Model model) 
	{
		Users user = (Users)session.getAttribute(Redirect.loginSession);
		if(user == null)
		{
			return Redirect.home;
		}
		UsersDto dto = new UsersDto();
		dto.setNickName(user.getNickName());
		dto.setRealName(user.getRealName());
		dto.setPhoneNumber(user.getPhoneNumber());
		//html을 꾸며줄 클래스 넣기
		model.addAttribute("UsersDto", dto);
		//수정 페이지로 이동
		return "ModifyUser";
	}
	
	@PostMapping(value = "ModifyUser")
	public String modifyUserPost(HttpSession session, Model model, UsersDto userDto)
	{
		//로그인 되어 있는 사람의 정보
		Users user = (Users)session.getAttribute(Redirect.loginSession);
//		//로그인 세션이 없는 상태
//		if(user == null)
//		{
//			return Redirect.home;
//		}
		System.out.println(user.getRealName());
//		userDto.setId(user.getId());
//		userDto.setPassword(user.getPassword());
//		//수정될 정보
		if(userService.UpdateUserDto(userDto))
		{
			//정보 수정 성공
		}
		else
		{
			//정보 수정 실패
		}
		//정보 수정
		return "MyPage";
	}

	// 비밀번호 변경
	@GetMapping(value = "ChangePassword")
	public String changePassword(Model model, UsersDto userDto) 
	{
		userService.UpdateUserDto(userDto);
		return "ChangePassword";
	}

	// 동물프로필
	@GetMapping(value = "AnimalProfile")
	public String animalProfile() 
	{
		return "AnimalProfile";
	}

	// 프로필 추가
	@GetMapping(value = "AddProfile")
	public String addProfile() 
	{
		return "AddProfile";
	}

	// 다마
	@GetMapping(value = "Damagochi")
	public String damagochi() 
	{
		return "Damagochi";
	}

	// 탈퇴
	@GetMapping(value = "Withdrawal")
	public String withdrawal() 
	{
		return "Withdrawal";
	}
}
