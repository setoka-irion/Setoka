package com.practice.setoka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.practice.setoka.Enum.GameResult;
import com.practice.setoka.dao.Users;
import com.practice.setoka.service.RPSService;
import com.practice.setoka.springSecurity.CustomUserDetails;

@Controller
public class RPSController {

	@Autowired
	private RPSService rpsService;
	
	@GetMapping(value = "RPS")
	public String RPSForm(Model model)
	{
		return "RPS";
	}
	
	@PostMapping(value ="playRps")
	public String ResultRPS(@RequestParam("choice") String choice, @RequestParam("point") int point,
			@AuthenticationPrincipal CustomUserDetails authUser, RedirectAttributes redirectAttributes)
	{
		Users user = authUser.getUser();
		
		GameResult result = rpsService.Play(choice, point, user);
		redirectAttributes.addFlashAttribute("result", result.getDisplayName());
		return "redirect:/RPS";
	}
}
