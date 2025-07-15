package com.practice.setoka.dto;

import java.time.LocalDateTime;

public class CommentInfoDto {
		
	//comment
		int num;
		int userNum;
		int boardNum;
		String content;
		int likes;
		LocalDateTime localDateTime;
		int status;
		
	//users
		String nickname;
		String grade;
		
	//board
		int type;
		
}
