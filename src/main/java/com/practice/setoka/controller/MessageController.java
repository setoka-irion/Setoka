package com.practice.setoka.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.practice.setoka.Enum.Item;
import com.practice.setoka.dao.Message;
import com.practice.setoka.dto.MessageDto;
import com.practice.setoka.service.MessageService;
import com.practice.setoka.service.UserService;
import com.practice.setoka.springSecurity.CustomUserDetails;

@Controller
public class MessageController {
	@Autowired
	private MessageService messageService;
	@Autowired
	private UserService userService;
	
	@GetMapping(value = "CreateLetter")
	public String CreateLetter(Model model, @AuthenticationPrincipal CustomUserDetails authUser, RedirectAttributes redirectAttributes)
	{
		String email = (String)redirectAttributes.getAttribute("email"); 
		if(email != null)
		{
			model.addAttribute("receiver", email);
		}
		model.addAttribute("itemTypes", Item.values());
		model.addAttribute("max", authUser.getUser().getPoint());
		System.out.println(authUser.getUser().getPoint());
		return "Message/CreateLetter";
	}
	
	@PostMapping(value = "SendMessage")
	public String sendMessage(MessageDto dto, @AuthenticationPrincipal CustomUserDetails authUser)
	{
		//포인트가 부족한 경우
		if(authUser.getUser().getPoint() < dto.getItem_Value())
			return "redirect:/CreateLetter";
		
		dto.setSender(authUser.getUser().getId());
		
		//아이템이 없는 경우
		if(dto.getItem_Type() == null)
			dto.setItem_Value(0);
		
		if(messageService.sendMessage(dto))
		{
			switch(dto.getItem_Type())
			{
			case POINT:
				int newPoint = authUser.getUser().getPoint() - dto.getItem_Value();
				userService.userPointUpdate(dto.getSender(), newPoint);
				authUser.getUser().setPoint(newPoint);
				break;
			}
		}
		
		return "redirect:/CreateLetter";
	}
	
	@GetMapping(value = "MessageList")
	public String messageList(Model model, @AuthenticationPrincipal CustomUserDetails authUser)
	{
		List<Message> list = messageService.receiverSelect(authUser.getUser().getId());
		model.addAttribute("messageList", list);
		return "Message/messageList";
	}
	
	@GetMapping(value = "GettingGift")
	public String GettingGift(@RequestParam("messageNum") int num, @AuthenticationPrincipal CustomUserDetails authUser)
	{
		messageService.GettingMessage(num, authUser.getUser());
		return "redirect:/MessageList";
	}
}
