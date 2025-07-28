package com.practice.setoka.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.practice.setoka.Enum.MessageStatus;
import com.practice.setoka.dao.Message;
import com.practice.setoka.dto.MessageDto;


@Mapper
public interface MessageMapper {
	
	//메세지 넣기
	public boolean insertMessage(MessageDto dto);
	//모드 메세지 받기
	public List<Message> selectAllMessage();
	//num으로 하나 리턴 받기
	public Message selectToNum(@Param("num") int num);
	//num이 보내는 사람인 전부
	public List<Message> selectSenderMessage(@Param("id") String id);
	//num이 받는 사람인 전부
	public List<Message> selectReceiverMessage(@Param("id") String id);
	//update status
	public int updateMessageStatus(@Param("num") int num, @Param("status") MessageStatus status);
}
