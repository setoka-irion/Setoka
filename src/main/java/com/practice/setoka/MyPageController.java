package com.practice.setoka;

import org.springframework.web.bind.annotation.GetMapping;

public class MyPageController 
{
	// 마이페이지
	@GetMapping(value = "MyPage")
	public String myPage() 
	{
		return "MyPage";
	}

	// 개인정보수정
	@GetMapping(value = "ModifyUser")
	public String modifyUser() 
	{
		return "ModifyUser";
	}

	// 비밀번호 변경
	@GetMapping(value = "ChangePassword")
	public String changePassword() 
	{
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
