package com.practice.setoka.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.setoka.dao.Message;
import com.practice.setoka.dto.MessageDto;
import com.practice.setoka.mapper.MessageMapper;

@Service
public class MessageService {
	@Autowired
	private MessageMapper messageMapper;
	@Autowired
	private UserService userService;
	
	public void sendMessage(MessageDto dto)
	{
		if(userService.selectByID(dto.getReceiver()) == null)
			return;
		if(userService.selectByID(dto.getSender()) == null)
			return;
		messageMapper.insertMessage(dto);
	}
	
	public List<Message> sendSelect(String userId)
	{
		return messageMapper.selectSenderMessage(userId);
	}
	
	public List<Message> receiverSelect(String userId)
	{
		return messageMapper.selectReceiverMessage(userId);
	}
}
