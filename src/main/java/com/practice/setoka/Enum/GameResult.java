package com.practice.setoka.Enum;

public enum GameResult {
	LOSE("패배"),
	WIN("승리"),
	DRAW("무승부");
	
	private final String displayName;
	GameResult(String displayName)
	{
		this.displayName = displayName;
	}
	
	public String getDisplayName()
	{
		return displayName;
	}
}
