package com.practice.setoka;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController 
{
	//홈화면
	@GetMapping(value = "/")
	public String home()
	{
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
	
}
