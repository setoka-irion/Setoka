package com.practice.setoka.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.practice.setoka.Redirect;
import com.practice.setoka.dao.Users;
import com.practice.setoka.service.UserService;
import com.practice.setoka.springSecurity.CustomUserDetails;

@Controller
public class AllUsersController {
	
	@Autowired
	private UserService userservice;

	@GetMapping("AllUsers")
	public String PrintAllUsers(Model model, @AuthenticationPrincipal CustomUserDetails authUser, RedirectAttributes redirectAttributes) {
		List<Users> allUsers = userservice.selectAllUsers();
        Users loginData = authUser.getUser();
        
        String id = loginData.getId();
        
		if("asd".equals(id)) {
			model.addAttribute("allUsers", allUsers);
			return "AllUsers";
		}
		else {
			redirectAttributes.addFlashAttribute("message", "관리자 이외에는 접근할 수 없습니다.");
			return Redirect.home;
		}
	}
}
