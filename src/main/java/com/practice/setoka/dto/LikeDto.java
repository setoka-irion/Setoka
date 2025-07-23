package com.practice.setoka.dto;

public class LikeDto {
	
	//좋아요 기능 liked테이블
	private int userNum;
	private int boardNum;
	
	// 실질적 좋아요 증가 board테이블
	private int likes;
	
	public LikeDto(int use, int boa)
	{
		userNum=use;
		boardNum=boa;
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
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	 
	
}
