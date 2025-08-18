package com.practice.setoka.dto;

public class CommentLikeDto {
	
	//좋아요 기능 commentlike테이블
	int userNum;
	int commentNum;
	
	// 실질적 좋아요 증가 comment테이블
	int likes;
	
	public CommentLikeDto(int use, int boa)
	{
		userNum=use;
		commentNum=boa;
	}
	
	public int getUserNum() {
		return userNum;
	}
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	
	
}
