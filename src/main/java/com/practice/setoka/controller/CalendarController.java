package com.practice.setoka.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class CalendarController {

	@GetMapping(value = "Calendar")
	public String Calendar() {
		
		return "Calendar";
	}
	
}
