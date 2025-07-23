package com.practice.setoka.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.practice.setoka.Redirect;
import com.practice.setoka.Enum.Grade;
import com.practice.setoka.dao.Users;
import com.practice.setoka.service.UserService;
import com.practice.setoka.springSecurity.CustomUserDetails;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	UserService userService;
	// 홈화면
	@GetMapping(value = "/")
	public String home(HttpSession session, Model model, @AuthenticationPrincipal CustomUserDetails authUser) {
		if (authUser != null) {
			Users loginData = authUser.getUser();
			if (loginData != null) {
				model.addAttribute("grade", loginData.getGrade());
			}
		}

		return "home";
	}

	@GetMapping(value = "PetPlaces")
	public String petPlaces() {
		return "PetPlaces";
	}

	@GetMapping("AllUsers")
	public String PrintAllUsers(Model model, @AuthenticationPrincipal CustomUserDetails authUser
			) {
		List<Users> allUsers = userService.selectAllUsers();
		Users loginData = authUser.getUser();

		Grade grade = loginData.getGrade();

		if (grade.equals(Grade.관리자)) {
			model.addAttribute("allUsers", allUsers);
			return "AllUsers";
		} else {
			return Redirect.home;
		}
	}
}
