package com.practice.setoka.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.setoka.Enum.GameResult;
import com.practice.setoka.Enum.RPSEnum;
import com.practice.setoka.dao.Users;
import com.practice.setoka.mapper.RPSMapper;

@Service
public class RPSService {
	//0 패배 1 승리 2 무승부
	GameResult[][] result = new GameResult[][] {
			{GameResult.DRAW, GameResult.LOSE, GameResult.WIN},
			{GameResult.WIN, GameResult.DRAW, GameResult.LOSE},
			{GameResult.LOSE, GameResult.WIN, GameResult.DRAW}	
	};
	
	@Autowired
	private UserService userService;
	@Autowired
	private RPSMapper rpsMapper;
	
	public GameResult Play(String choice, int point, Users user)
	{
		Random random = new Random();
		int ran = random.nextInt(3);
		int ch = 0;
		if(choice.equals("SCISSORS"))
		{
			ch = 0;
		}
		else if(choice.equals("ROCK"))
		{
			ch = 1;
		}
		else if(choice.equals("PAPER"))
		{
			ch = 2;
		}
		
		GameResult re = result[ch][ran];
		//유저 포인트 증가, 감소
		switch(re)
		{
		case LOSE:	//패배
			point *= -1;
			break;
		case WIN:	//승리
			break;
		case DRAW:	//무승부
			point = 0;
			break;
		}
		
		user.setPoint(user.getPoint() + point);

		rpsMapper.insertRPSResult(user.getNum(), RPSEnum.getEnum(ch), RPSEnum.getEnum(ran), re, point);
		userService.userPointUpdate(user.getId(), user.getPoint());
		return re;
	}
}
