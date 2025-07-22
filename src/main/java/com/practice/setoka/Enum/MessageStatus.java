package com.practice.setoka.Enum;

public enum MessageStatus {
	UNREAD("안읽음"),
	READ("읽음"),
	TAKE("받음"),
	DELETE("삭제");
	
	private final String displayName;
	MessageStatus(String displayName)
	{
		this.displayName = displayName;
	}
	
	public String getDisplayName()
	{
		return displayName;
	}
}
