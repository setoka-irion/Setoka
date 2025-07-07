package com.practice.setoka.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.practice.setoka.dao.Users;
import com.practice.setoka.dto.UsersDto;

@Mapper
public interface UserMapper 
{
	//num에 해당하는 user 하나 리턴
	Users selectUserByNum(@Param("num") int num);
	//id로 유저 찾기
	Users selectUserByID(@Param("id") String id);
	//id password 로 유저 찾기
	Users selectUserByIDPassword(@Param("id") String id, @Param("password") String password);
	//모든 유저를 리턴
	List<Users> selectAllUsers();
	//유저 넣기
	boolean insertUserToDto(UsersDto dto);
	//유저 수정
	void updateUser(UsersDto dto);
	//유저 삭제
	void deleteUser(@Param("num") int num);
	
}
