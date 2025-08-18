package com.practice.setoka.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.practice.setoka.Redirect;
import com.practice.setoka.Upload;
import com.practice.setoka.Enum.Privileges;
import com.practice.setoka.Enum.Status;
import com.practice.setoka.dao.Carousel;
import com.practice.setoka.dao.Users;
import com.practice.setoka.dto.BoardWithUserDto;
import com.practice.setoka.dto.UsersDto;
import com.practice.setoka.service.BoardService;
import com.practice.setoka.service.CarouselService;
import com.practice.setoka.service.UserService;
import com.practice.setoka.springSecurity.CustomUserDetails;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	UserService userService;
	@Autowired
	Upload upload;
	@Autowired
	BoardService boardService;
	@Autowired
	CarouselService carouselService;
	
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
		
		List<Carousel> carouselList = carouselService.selectAllCarousel();
		model.addAttribute("carousel", carouselList);
		
		return "home";
	}
	
//	@GetMapping("/error")
//	public String errorPage(HttpServletRequest request) {
//	    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//	    System.out.println("에러 상태: " + status);
//	    if(status == null)
//	    	return "redirect:/";
//	    return "error";
//	}
	
	
	
	
	
	
	

	@GetMapping(value = "PetPlaces")
	public String petPlaces() {
		return "PetPlaces";
	}
	
	
	
	//유저 관리
	@GetMapping("/admin/userlist")
	public String PrintAllUsers(Model model, @AuthenticationPrincipal CustomUserDetails authUser
			) {
		List<Users> allUsers = userService.selectAllUsers();
		Users loginData = authUser.getUser();

		Privileges privileges = loginData.getPrivileges();

		if (privileges.equals(Privileges.관리자)) {
			model.addAttribute("privilegeOptions", Privileges.values()); // ⭐️ enum 전체 추가
			model.addAttribute("allUsers", allUsers);
			return "AllUsers";
		} else {
			return Redirect.home;
		}
	}
	
	@PostMapping(value = "admin/updateStatus")
	public String updateUserState(@AuthenticationPrincipal CustomUserDetails authUser,
			@RequestParam("userId") String userId, @RequestParam("status") Status status)
	{
		//지금 접속중인 사람
		Users user = authUser.getUser();
		if(!user.isAdmin())
			return Redirect.home;
		
		//update할 유저
		user = userService.selectByID(userId);
		user.setStatus(status);
		userService.updateUserDto(new UsersDto(user));
		return "redirect:/admin/userlist";
	}
	
	@PostMapping(value = "/admin/updatePrivileges")
	public String updatePrivileges(@AuthenticationPrincipal CustomUserDetails authUser,
			@RequestParam("userIdId") String userId, @RequestParam("privilege") Privileges privilege)
	{
		Users admin = authUser.getUser();
		if(!admin.isAdmin())
			return Redirect.home;
		Users user = userService.selectByID(userId);
		user.setPrivileges(privilege);
		userService.updateUserDto(new UsersDto(user));
		return "redirect:/admin/userlist";
	}
	
	@PostMapping(value = "/admin/updateExp")
	public String updateExp(@AuthenticationPrincipal CustomUserDetails authUser,
			@RequestParam("userIdIdId") String userId, @RequestParam("exp") int exp)
	{
		Users admin = authUser.getUser();
		if(!admin.isAdmin())
			return Redirect.home;
		Users user = userService.selectByID(userId);
		user.setExp(exp);
		userService.updateUserDto(new UsersDto(user));
		return "redirect:/admin/userlist";
	}
	
	
	//홈화면 carousel 관리
	@GetMapping(value = "/admin/carousel")
	public String Carousel(@AuthenticationPrincipal CustomUserDetails authUser, Model model)
	{
		Users user = authUser.getUser();
		if(!user.isAdmin())
			return Redirect.home;
		
		List<Carousel> list = carouselService.selectAllCarousel();
		model.addAttribute("list", list);

		return "Carousel";
	}
	
	@PostMapping("/admin/carouselInsert")
	public String insertCarousel(@AuthenticationPrincipal CustomUserDetails authUser,
			@RequestParam("profile") MultipartFile file,
	                             @RequestParam("linkUrl") String linkUrl) throws IOException {
		Users user = authUser.getUser();
		if(!user.isAdmin())
			return Redirect.home;
		
	    if (!file.isEmpty()) {
	        // DB에 저장
	    	carouselService.insertCarousel(file, linkUrl);
	    }

	    return "redirect:/admin/carousel"; // 다시 관리 페이지로 이동
	}
	
	@PostMapping(value = "/admin/carousel/delete")
	public String deleteCarousel(@AuthenticationPrincipal CustomUserDetails authUser,
			@RequestParam("id") int id, @RequestParam("img") String img)
	{
		Users user = authUser.getUser();
		if(!user.isAdmin())
			return Redirect.home;
		
		carouselService.deleteCarousel(id, img);
		return "redirect:/admin/carousel";
	}
	
	
	//기본 이미지 관리
	@GetMapping(value = "/admin/default")
	public String defaultImage(@AuthenticationPrincipal CustomUserDetails authUser)
	{
		Users user = authUser.getUser();
		if(!user.isAdmin())
			return Redirect.home;
		
		return "Default";
	}
	
	@PostMapping(value = "/admin/default/board")
	public String boardDefaultImage(@AuthenticationPrincipal CustomUserDetails authUser,
			@RequestParam("profile") MultipartFile file)
	{
		Users user = authUser.getUser();
		if(!user.isAdmin())
			return Redirect.home;
		
		upload.SetDefaultBoard(file);
		return "Default";
	}
	
	@PostMapping(value = "/admin/default/animal")
	public String boardDefaultAnimal(@AuthenticationPrincipal CustomUserDetails authUser,
			@RequestParam("profile") MultipartFile file)
	{
		Users user = authUser.getUser();
		if(!user.isAdmin())
			return Redirect.home;
		
		upload.SetDefaultAnimal(file);
		return "Default";
	}
	
	@PostMapping(value = "/admin/default/profile")
	public String boardDefaultProfile(@AuthenticationPrincipal CustomUserDetails authUser,
			@RequestParam("profile") MultipartFile file)
	{
		Users user = authUser.getUser();
		if(!user.isAdmin())
			return Redirect.home;
		
		upload.SetDefaultProfile(file);
		return "Default";
	}
	
	//게시물 신고 관리
	@GetMapping(value = "/admin/reportBoardList")
	public String reportBoardList(@AuthenticationPrincipal CustomUserDetails authUser, Model model)
	{
		Users user = authUser.getUser();
		if(!user.isAdmin())
			return Redirect.home;
		
		model.addAttribute("list", boardService.reportBoardList(1)) ;
		
		
		return "reportBoardList";
	}
}
