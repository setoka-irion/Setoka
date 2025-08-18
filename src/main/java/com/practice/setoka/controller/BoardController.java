package com.practice.setoka.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController 
{
	// 게시판
	@GetMapping(value = "BoardPage")
	public String board()
	{
		return "Board";
	}
	
	//애견자랑
	@GetMapping(value = "AnimalPridePage")
	public String animalPride()
	{
		return "AnimalPride";
	}
	
	//산책로 추천
	@GetMapping(value = "WalkTrailPage")
	public String walkTrail()
	{
		return "WalkTrail";
	}
	
	//중고물품
	@GetMapping(value = "UsedGoodsPage")
	public String usedGoods()
	{
		return "UsedGoods";
	}
	
	//노하우
	@GetMapping(value = "KnowHowPage")
	public String knowHow()
	{
		return "KnowHow";
	}
	
	//분양
	@GetMapping(value = "AdoptPage")
	public String adopt()
	{
		return "Adopt";
	}
	
	//검색기능
	
}
