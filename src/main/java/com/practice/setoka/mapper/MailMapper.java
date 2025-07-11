package com.practice.setoka.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.practice.setoka.dao.PasswordReset;
import com.practice.setoka.dao.VerifyCode;

@Mapper
public interface MailMapper {
	//num으로 가져오기
	VerifyCode selectVerifyCode(@Param("num")int num);
	//email로 가져오기
	VerifyCode selectVerifyCodeByID(@Param("email")String email);
	//email로 가져오기 (5분 이내의 것만 가져옴)
	VerifyCode selectVerifyCodeByIDLimit(@Param("email")String email);
	
	//code를 집어넣기
	boolean insertCode(@Param("email") String email, @Param("code") int code);
	//이미 있는 email 이라면 update로 code 와 registDate를 수정
	boolean updateVerifyCode(@Param("email") String email, @Param("code") int code);

	//이메일이 없을때 넣기 위해
	boolean insertPasswordReset(@Param("email") String email);
	//이메일이 있다면 update (registDate를)
	boolean updatePasswordReset(@Param("email") String email);
	//이메일이 있는지 확인을 위해
	PasswordReset selectPasswordReset(@Param("email") String email);
	//이메일이 있는지 확인을 위해 (5분 제한)
	PasswordReset selectPasswordResetLimit(@Param("email") String email);
}
