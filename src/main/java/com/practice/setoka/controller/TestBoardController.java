//package com.practice.setoka.controller;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import com.practice.setoka.JsonFileWriter;
//import com.practice.setoka.service.TestService;
//
//@Controller
//public class TestBoardController 
//{
//	@Autowired
//	TestService testservice;
//	// 게시판
//	@GetMapping(value = "testboard")
//	public String board(Model model)
//	{
//		Map<String, Object> mapData = new HashMap<>();
//		mapData.put("제목", "어린왕자");
//		mapData.put("내용", "왕자이야기");
//		JsonFileWriter.saveJsonToFile(mapData, "C:/images/test.json");
//		
//		try
//		{
//			testservice.insertContent("C:/images/test.json");
//			mapData = JsonFileWriter.readJsonFileToMap(testservice.selectContent(4));
//			mapData.forEach((key, value) -> {
//			    System.out.println("key = " + key + ", value = " + value);
//			});
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		
//		return "TestBoard";
//	}
//	
//	
//}
