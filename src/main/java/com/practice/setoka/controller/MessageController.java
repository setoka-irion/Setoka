package com.practice.setoka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.practice.setoka.Redirect;
import com.practice.setoka.dao.Users;
import com.practice.setoka.dto.MessageDto;
import com.practice.setoka.service.MessageService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MessageController {
	@Autowired
	private MessageService messageService;
	
	@PostMapping(value = "SendMessage")
	public String sendMessage(MessageDto dto)
	{
		messageService.sendMessage(dto);
		return "";
	}
	
	@GetMapping(value = "MessageList")
	public String messageList(HttpSession session, Model model)
	{
		Users user = (Users)session.getAttribute(Redirect.loginSession);
		if(user == null)
			return Redirect.home;
		
		model.addAttribute("messageList", messageService.receiverSelect(user.getNum()));
		return "";
	}
}
