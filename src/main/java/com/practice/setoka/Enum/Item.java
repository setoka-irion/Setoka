package com.practice.setoka.Enum;

public enum Item {
	POINT("포인트");
 
	
	
	private final String displayName;
	Item(String displayName)
	{
		this.displayName = displayName;
	}
	
	public String getDisplayName()
	{
		return displayName;
	}
}
