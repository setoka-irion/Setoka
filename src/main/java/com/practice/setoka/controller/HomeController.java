package com.practice.setoka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.practice.setoka.dao.Users;
import com.practice.setoka.service.EmailService;
import com.practice.setoka.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController 
{
	@Autowired
	private UserService userService;
	//홈화면
	@GetMapping(value = "/")
	public String home(HttpSession session, Model model)
	{
		Users loginData = (Users)session.getAttribute("LoginSession");
		if(loginData != null)
			model.addAttribute("login", loginData.getNickName());
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
	
	@GetMapping(value = "PetPlaces")
	public String petPlaces()
	{
		System.out.println("test");
		return "PetPlaces2";
	}
}
