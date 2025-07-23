package com.practice.setoka.Enum;

public enum RPSEnum {
	ROCK("주먹"),
	PAPER("보"),
	SCISSORS("가위");
	
	private final String displayName;
	RPSEnum(String displayName)
	{
		this.displayName = displayName;
	}
	
	public static RPSEnum getEnum(int index)
	{
		switch(index)
		{
		case 0:
			return SCISSORS;
		case 1:
			return ROCK;
		case 2:
			return PAPER;
		}
		return null;
	}
	
	public String getDisplayName()
	{
		return displayName;
	}
}
