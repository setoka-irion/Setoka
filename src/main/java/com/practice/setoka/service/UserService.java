package com.practice.setoka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.setoka.Redirect;
import com.practice.setoka.dao.Users;
import com.practice.setoka.dto.UsersDto;
import com.practice.setoka.mapper.UserMapper;


@Service
public class UserService 
{
	@Autowired
	private UserMapper userMapper;
	
	//num 으로 유저 리턴
	public Users SelectUser(int num)
	{
		return userMapper.selectUserByNum(num);
	}
	
	//id로 유저 리턴
	public Users SelectByID(String id)
	{
		return userMapper.selectUserByID(id);
	}
	
	//id password로 유저 리턴
	public Users SelectByIdPassword(String id, String password)
	{
		return userMapper.selectUserByIDPassword(id, password);
	}
	
	//이름 중복검사 (이미 있는 이름이면 reutrn true;)
	public boolean ExistsByName(String id)
	{
		Users user = SelectByID(id);
		return user != null;
	}
	
	//비밀번호 조건 검사 (조건을 만족하면 return true)
	public boolean PasswordInvalid(String password)
	{
		return password != null && password.matches(Redirect.passwordInvalidPattern);
	}
	
	//유저 넣기
	public boolean InsertUserNomal(UsersDto dto)
	{
		return userMapper.insertUserToDto(dto);
	}
	
	//어드민 넣기
	public boolean InsertUserAdmin(UsersDto dto)
	{
		dto.setGrade("관리자");
		return userMapper.insertUserToDto(dto);
	}
}
