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
	
	public void sendMessage(MessageDto dto)
	{
		messageMapper.insertMessage(dto);
	}
	
	public List<Message> sendSelect(int userNum)
	{
		return messageMapper.selectSenderMessage(userNum);
	}
	
	public List<Message> receiverSelect(int userNum)
	{
		return messageMapper.selectReceiverMessage(userNum);
	}
}
