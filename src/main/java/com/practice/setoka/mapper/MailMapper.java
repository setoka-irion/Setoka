package com.practice.setoka.mapper;

import org.apache.ibatis.annotations.Param;

import com.practice.setoka.dao.VerifyCode;

public interface MailMapper {
	VerifyCode selectVerifyCode(@Param("num")int num);
//	VerifyCode selectVerifyCodeByID(@Param("email")String email);
}
