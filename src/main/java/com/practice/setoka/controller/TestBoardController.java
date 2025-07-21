package com.practice.setoka.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.practice.setoka.JsonFileWriter;

@Controller
public class TestBoardController 
{
	
	// 게시판
	@GetMapping(value = "TestBoard")
	public String board(Model model)
	{
		Map<String, Object> mapData = new HashMap<>();
		mapData.put("test", "value");
		JsonFileWriter.saveJsonToFile(mapData, "C:/images/test.json");
		
		try
		{
			mapData = JsonFileWriter.readJsonFileToMap("C:/images/test.json");
			mapData.forEach((key, value) -> {
			    System.out.println("key = " + key + ", value = " + value);
			});
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return "TestBoard";
	}
}
