package com.practice.setoka.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.practice.setoka.Enum.Item;
import com.practice.setoka.dao.Message;
import com.practice.setoka.dto.MessageDto;
import com.practice.setoka.service.MessageService;
import com.practice.setoka.springSecurity.CustomUserDetails;

@Controller
public class MessageController {
	@Autowired
	private MessageService messageService;
	
	@GetMapping(value = "CreateLetter")
	public String CreateLetter(Model model)
	{
		String id = "bit393298@gmail.com";
		
		model.addAttribute("id", id);
		model.addAttribute("itemTypes", Item.values());
		return "CreateLetter";
	}
	
	@PostMapping(value = "SendMessage")
	public String sendMessage(MessageDto dto, @AuthenticationPrincipal CustomUserDetails authUser)
	{
		dto.setSender("bit393298@gmail.com");
		dto.setReceiver("bit393298@gmail.com");
		if(dto.getItem_Type() == null)
			dto.setItem_Value(0);
		messageService.sendMessage(dto);
		return "redirect:/CreateLetter";
	}
	
	@GetMapping(value = "MessageList")
	public String messageList(Model model, @AuthenticationPrincipal CustomUserDetails authUser)
	{
		List<Message> list = messageService.receiverSelect(authUser.getUser().getId());
		System.out.println(list.size());
		model.addAttribute("messageList", list);
		return "messageList";
	}
}
