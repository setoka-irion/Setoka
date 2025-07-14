package com.practice.setoka.Enum;

public enum Status {
	정상,
	삭제;
	
	public String Get(int index)
	{
		switch(index)
		{
		case 0:
			return "정상";
		case 1:
			return "삭제";
		}
		
		return "";
	}
}
