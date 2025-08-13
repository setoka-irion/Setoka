package com.practice.setoka.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.practice.setoka.Redirect;
import com.practice.setoka.Upload;
import com.practice.setoka.Enum.Privileges;
import com.practice.setoka.dao.Users;
import com.practice.setoka.dto.BoardWithUserDto;
import com.practice.setoka.dto.CarouselItem;
import com.practice.setoka.service.BoardService;
import com.practice.setoka.service.UserService;
import com.practice.setoka.springSecurity.CustomUserDetails;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	UserService userService;
	@Autowired
	Upload upload;
	@Autowired
	BoardService boardService;
	
	// 홈화면
	@GetMapping(value = "/")
	public String home(HttpSession session, Model model) {

		List<BoardWithUserDto> list = boardService.popularPosts(1);
		if (!list.isEmpty()) {
		    model.addAttribute("Adopt", list.subList(0,  Math.min(list.size(), 3)));
		}
		
		list = boardService.popularPosts(2);
		if (!list.isEmpty()) {
		    model.addAttribute("AnimalPride", list.subList(0,  Math.min(list.size(), 3)));
		}
		
		list = boardService.popularPosts(3);
		if (!list.isEmpty()) {
		    model.addAttribute("WalkTrail", list.subList(0,  Math.min(list.size(), 3)));
		}
		
		list = boardService.popularPosts(4);
		if (!list.isEmpty()) {
		    model.addAttribute("UsedGoods", list.subList(0,  Math.min(list.size(), 3)));
		}
		
		list = boardService.popularPosts(5);
		if (!list.isEmpty()) {
		    model.addAttribute("Knowhow", list.subList(0, Math.min(list.size(), 3)));
		}
		
		List<CarouselItem> carouselList = new ArrayList<CarouselItem>();
		carouselList.add(new CarouselItem("/images/Carousel/Carousel1.png", "/Adopt"));
		carouselList.add(new CarouselItem("/images/Carousel/Carousel2.png", ""));
		model.addAttribute("carousel", carouselList);
		
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

		Privileges privileges = loginData.getPrivileges();

		if (privileges.equals(Privileges.관리자)) {
			model.addAttribute("allUsers", allUsers);
			return "AllUsers";
		} else {
			return Redirect.home;
		}
	}
}
