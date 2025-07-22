package com.practice.setoka.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.setoka.dao.Users;
import com.practice.setoka.dto.UsersDto;

@Service
public class RPSService {
	//0 패배 1 승리 2 무승부
	int[][] result = new int[][] {
			{2, 0, 1},
			{1, 2, 0},
			{0, 1, 2}	
	};
	
	@Autowired
	private UserService userService;
	
	public String Play(String choice, int point, Users user)
	{
		Random random = new Random();
		int ran = random.nextInt(3);
		int ch = 0;
		if(choice.equals("SCISSORS"))
			ch = 0;
		else if(choice.equals("ROCK"))
			ch = 1;
		else if(choice.equals("PAPER"))
			ch = 2;
		
		int re = result[ch][ran];
		String s = "";
		//유저 포인트 증가, 감소
		switch(re)
		{
		case 0:	//패배
			user.setPoint(user.getPoint() - point);
			System.out.println("패배");
			s = "패배";
			break;
		case 1:	//승리
			user.setPoint(user.getPoint() + point);
			System.out.println("승리");
			s = "승리";
			break;
		case 2:	//무승부
			System.out.println("무승부");
			s = "무승부";
			break;
		}
		System.out.println(user.getPoint());
		userService.userPointUpdate(user.getId(), user.getPoint());
		return s;
	}
}
