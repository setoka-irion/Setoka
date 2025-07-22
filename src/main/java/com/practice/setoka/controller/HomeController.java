package com.practice.setoka.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.practice.setoka.dao.Users;
import com.practice.setoka.springSecurity.CustomUserDetails;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController 
{
	//홈화면
	@GetMapping(value = "/")
	public String home(HttpSession session, Model model, @AuthenticationPrincipal CustomUserDetails authUser)
	{
//		Users loginData = (Users) authUser.getUser();
//		SessionUrlHandler.save(session, "");
//		if(loginData != null)
//		{
//			model.addAttribute("login", loginData.getNickName());
//			model.addAttribute("path", loginData.getProfilePath());
//			System.out.println(loginData.getProfilePath());
//		}
		if(authUser != null) {
	        Users loginData = authUser.getUser();
	        if(loginData != null) {
	            model.addAttribute("login", loginData.getNickName());
	            model.addAttribute("path", loginData.getProfilePath());
	            model.addAttribute("id", loginData.getId());
	        }
	    }
			
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
		return "PetPlaces";
	}
	

}
