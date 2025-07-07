package com.practice.setoka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.practice.setoka.dao.Users;
import com.practice.setoka.service.UserService;

@Controller
public class HomeController 
{
	@Autowired
	private UserService userService;
	//홈화면
	@GetMapping(value = "/")
	public String home(Model model)
	{
		System.out.println("home 요청");
		Users user = userService.SelectUser(1);
		System.out.println(user.getNickName());
		model.addAttribute("now", user.getNickName());
		return "home";
	}
	
	//병원
	@GetMapping(value = "Hospital")
	public String hospital()
	{
		return "Hospital";
	}
	
	//동물카페
	@GetMapping(value = "AnimalCafe")
	public String animalCafe()
	{
		return "AnimalCafe";
	}
	
	//출석체크
	@GetMapping(value = "Attendance")
	public String attendance()
	{
		return "Attendance";
	}
	
		@GetMapping(value = "pet")
		public String petPlaces()
		{
			return "petPlaces";
		}
}
