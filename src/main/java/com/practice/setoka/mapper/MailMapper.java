package com.practice.setoka.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.practice.setoka.dao.VerifyCode;

@Mapper
public interface MailMapper {
	VerifyCode selectVerifyCode(@Param("num")int num);
	VerifyCode selectVerifyCodeByID(@Param("email")String email);
	boolean insertCode(@Param("email") String email, @Param("code") int code);
}
