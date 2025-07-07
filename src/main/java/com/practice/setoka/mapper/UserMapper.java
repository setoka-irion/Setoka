package com.practice.setoka.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.practice.setoka.dao.Users;

@Mapper
public interface UserMapper 
{
	Users selectUserById(@Param("num") int num);
}
