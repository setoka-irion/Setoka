package com.practice.setoka.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.practice.setoka.Enum.Item;
import com.practice.setoka.Enum.MessageStatus;
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
	public String CreateLetter(Model model, @AuthenticationPrincipal CustomUserDetails authUser,
			RedirectAttributes redirectAttributes)
	{
//		String email = (String)redirectAttributes.getAttribute("email"); 
//		if(email != null)
//		{
//			model.addAttribute("receiver", email);
//		}
		model.addAttribute("itemTypes", Item.values());
		model.addAttribute("max", authUser.getUser().getPoint());
		return "Message/CreateLetter";
	}
	
	@PostMapping(value = "CreateLetter")
	public String CreateLetter(Model model, @AuthenticationPrincipal CustomUserDetails authUser,
			RedirectAttributes redirectAttributes,
			@RequestParam(value = "receiver", required = false) String receiver)
	{
		if(receiver != null)
			model.addAttribute("receiver", receiver);
		
//		String email = (String)redirectAttributes.getAttribute("email"); 
//		if(email != null)
//		{
//			model.addAttribute("receiver", email);
//		}
		model.addAttribute("itemTypes", Item.values());
		model.addAttribute("max", authUser.getUser().getPoint());
		return "Message/CreateLetter";
	}
	
	@PostMapping(value = "SendMessage")
	public String sendMessage(MessageDto dto, @AuthenticationPrincipal CustomUserDetails authUser)
	{
		//포인트가 부족한 경우
		if(authUser.getUser().getPoint() < dto.getItem_Value())
			return "redirect:/CreateLetter";
		
		dto.setSender(authUser.getUser().getId());
		
		if(dto.getItem_Type() == Item.NONE)
			dto.setItem_Value(0);
		
		if(messageService.sendMessage(dto))
		{
			if(dto.getItem_Type() == Item.POINT)
			{
				int newPoint = authUser.getUser().getPoint() - dto.getItem_Value();
				userService.userPointUpdate(dto.getSender(), newPoint);
				authUser.getUser().setPoint(newPoint);
			}
		}
		
		return "redirect:/CreateLetter";
	}
	
	@GetMapping(value = "MessageList")
	public String messageList(Model model, @AuthenticationPrincipal CustomUserDetails authUser)
	{
		//모든 받은 메세지를 보여주기
		List<Message> list = messageService.receiverSelect(authUser.getUser().getId());
		model.addAttribute("messageList", list);
		model.addAttribute("messageBoxType", "RECEIVED");

		return "Message/messageList";
	}
	
	@GetMapping(value = "sent")
	public String messageReceived(Model model, @AuthenticationPrincipal CustomUserDetails authUser)
	{
		//모든 보낸 메세지를 보여주기
		List<Message> list = messageService.sendSelect(authUser.getUser().getId());
		model.addAttribute("messageList", null);
		model.addAttribute("messageBoxType", "SENT");

		return "Message/messageList";
	}
	
	
	
	
	@GetMapping(value = "GettingGift")
	public String GettingGift(@RequestParam("messageNum") int num, @AuthenticationPrincipal CustomUserDetails authUser)
	{
		messageService.GettingMessage(num, authUser.getUser());
		return "redirect:/MessageList";
	}
	@GetMapping(value = "DeleteMessage")
	public String DeleteMessage(@RequestParam("messageNum") int num)
	{
		messageService.markMessageAsRead(num, MessageStatus.DELETE);
		return "redirect:/MessageList";
	}
	
	@PostMapping("/message/read/{messageNum}")
	@ResponseBody
	public ResponseEntity<?> markAsRead(@PathVariable("messageNum") int messageNum) {
	    boolean updated = messageService.markMessageAsRead(messageNum, MessageStatus.READ);
	    if (updated) {
	        return ResponseEntity.ok().build();
	    } else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update");
	    }
	}
}
