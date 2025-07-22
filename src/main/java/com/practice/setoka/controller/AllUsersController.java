package com.practice.setoka.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.practice.setoka.dao.Users;
import com.practice.setoka.service.UserService;

@Controller
public class AllUsersController {
	
	@Autowired
	private UserService userservice;

	@GetMapping("AllUsers")
	public String PrintAllUsers(Model model) {
		List<Users> allUsers = userservice.selectAllUsers();
		model.addAttribute("allUsers", allUsers);
	
		return "AllUsers";
	}
}
