package com.practice.setoka.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController 
{
	// 게시판
	@GetMapping(value = "Board")
	public String board()
	{
		return "Board";
	}
	
	//애견자랑
	@GetMapping(value = "AnimalPride")
	public String animalPride()
	{
		return "AnimalPride";
	}
	
	//산책로 추천
	@GetMapping(value = "WalkTrail")
	public String walkTrail()
	{
		return "WalkTrail";
	}
	
	//중고물품
	@GetMapping(value = "UsedGoods")
	public String usedGoods()
	{
		return "UsedGoods";
	}
	
	//노하우
	@GetMapping(value = "KnowHow")
	public String knowHow()
	{
		return "KnowHow";
	}
	
	//분양
	@GetMapping(value = "Adopt")
	public String adopt()
	{
		return "Adopt";
	}
	
	//검색기능
	
}
