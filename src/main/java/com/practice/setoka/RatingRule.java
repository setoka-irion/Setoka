package com.practice.setoka;

import com.practice.setoka.Enum.Grade;

public class RatingRule {
	public final static RatingRule ratingRule = new RatingRule(); 
	
	public Grade GetGrade(long exp)
	{
		if(exp > 20000)
			return Grade.다이아;
		else if(exp > 10000)
			return Grade.플레티넘;
		else if(exp > 5000)
			return Grade.골드;
		else if(exp > 1000)
			return Grade.실버;
		else
			return Grade.브론즈;
	}
}
