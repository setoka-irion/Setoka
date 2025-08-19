package com.practice.setoka.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.setoka.Enum.MessageStatus;
import com.practice.setoka.dao.Message;
import com.practice.setoka.dao.Users;
import com.practice.setoka.dto.MessageDto;
import com.practice.setoka.mapper.MessageMapper;

@Service
public class MessageService {
	@Autowired
	private MessageMapper messageMapper;
	@Autowired
	private UserService userService;
	
	public boolean sendMessage(MessageDto dto)
	{
		if(userService.selectByNickName(dto.getReceiver()) == null)
			return false;
		if(userService.selectByNickName(dto.getSender()) == null)
			return false;
		
		return messageMapper.insertMessage(dto);
	}
	
	public List<Message> sendSelect(String userId)
	{
		return messageMapper.selectSenderMessage(userId);
	}
	
	public List<Message> receiverSelect(String userId)
	{
		return messageMapper.selectReceiverMessage(userId);
	}
	
	public void GettingMessage(int num, Users user)
	{
		Message message = messageMapper.selectToNum(num);
		if(message.getItem_Type() == null)
		{
			messageMapper.updateMessageStatus(num, MessageStatus.TAKE);
			return;
		}
		
		switch(message.getItem_Type())
		{
		case POINT:
			userService.userPointUpdate(message.getReceiver(), user.getPoint() + message.getItem_Value());
			user.setPoint(user.getPoint() + message.getItem_Value());
			break;
			default:
				
				break;
		}
		messageMapper.updateMessageStatus(num, MessageStatus.TAKE);
	}
	
	public boolean markMessageAsRead(int num, MessageStatus status)
	{
		return messageMapper.updateMessageStatus(num, status) > 0;
	}
}
