package com.practice.setoka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.setoka.dao.Users;
import com.practice.setoka.dto.UsersDto;
import com.practice.setoka.mapper.UserMapper;


@Service
public class UserService 
{
	@Autowired
	private UserMapper userMapper;
	
	public Users SelectUser(int num)
	{
		return userMapper.selectUserByNum(num);
	}
	
	public Users SelectByID(String id)
	{
		return userMapper.selectUserByID(id);
	}
	
	public Users SelectByIdPassword(String id, String password)
	{
		return userMapper.selectUserByIDPassword(id, password);
	}
	
	public boolean InsertUserNomal(UsersDto dto)
	{
		return userMapper.insertUserToDto(dto);
	}
	
	public boolean InsertUserAdmin(UsersDto dto)
	{
		dto.setGrade("관리자");
		return userMapper.insertUserToDto(dto);
	}
}
