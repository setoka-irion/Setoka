package com.practice.setoka.dto;

public class CommentsDto {
	
	private int userNum;
	private int parantNum;
	private int boardNum;
	private String content;
	
	
	public int getparantNum() {
		return parantNum;
	}
	public void setparantNum(int parantNum) {
		this.parantNum = parantNum;
	}
	public int getUserNum() {
		return userNum;
	}
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}
	public int getBoardNum() {
		return boardNum;
	}
	public void setBoardNum(int boardNum) {
		this.boardNum = boardNum;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
