package com.practice.setoka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.setoka.dao.Users;
import com.practice.setoka.mapper.UserMapper;


@Service
public class UserService 
{
	@Autowired
	private UserMapper userMapper;
	
	public Users SelectUser(int num)
	{
		return userMapper.selectUserById(num);
	}
}
