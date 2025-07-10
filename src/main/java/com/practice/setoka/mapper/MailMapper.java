package com.practice.setoka.mapper;

import com.practice.setoka.dao.VerifyCode;

public interface MailMapper {
	VerifyCode selectVerifyCode(int num);
	VerifyCode selectVerifyCodeByID(String email);
}
