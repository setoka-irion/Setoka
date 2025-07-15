package com.practice.setoka.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.practice.setoka.dao.Message;
import com.practice.setoka.dto.MessageDto;


@Mapper
public interface MessageMapper {
	
	//메세지 넣기
	public boolean insertMessage(MessageDto dto);
	//모드 메세지 받기
	public List<Message> selectAllMessage();
	//num이 보내는 사람인 전부
	public List<Message> selectSenderMessage(@Param("num") int num);
	//num이 받는 사람인 전부
	public List<Message> selectReceiverMessage(@Param("num") int num);
}
