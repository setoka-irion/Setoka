package com.practice.setoka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.practice.setoka.dto.MessageDto;
import com.practice.setoka.service.MessageService;
import com.practice.setoka.springSecurity.CustomUserDetails;

@Controller
public class MessageController {
	@Autowired
	private MessageService messageService;
	
	@GetMapping(value = "CreateLetter")
	public String CreateLetter(String id)
	{
		return "CreateLetter";
	}
	
	@PostMapping(value = "SendMessage")
	public String sendMessage(MessageDto dto, @AuthenticationPrincipal CustomUserDetails authUser)
	{
		
		messageService.sendMessage(dto);
		return "";
	}
	
	@GetMapping(value = "MessageList")
	public String messageList(Model model, @AuthenticationPrincipal CustomUserDetails authUser)
	{
		model.addAttribute("messageList", messageService.receiverSelect(authUser.getUser().getNum()));
		return "";
	}
}
