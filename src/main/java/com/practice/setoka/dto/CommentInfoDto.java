package com.practice.setoka.dto;

import java.time.LocalDateTime;

public class CommentInfoDto {
		
	//comment
		int num;
		int userNum;
		int parentNum;
		int boardNum;
		String content;
		int likes;
		LocalDateTime localDateTime;
		int status;
		
	//users
		String nickname;
		String grade;
		
	//board
		int type;

		public int getNum() {
			return num;
		}

		public void setNum(int num) {
			this.num = num;
		}

		public int getUserNum() {
			return userNum;
		}

		public void setUserNum(int userNum) {
			this.userNum = userNum;
		}

		public int getParentNum() {
			return parentNum;
		}

		public void setParentNum(int parentNum) {
			this.parentNum = parentNum;
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

		public int getLikes() {
			return likes;
		}

		public void setLikes(int likes) {
			this.likes = likes;
		}

		public LocalDateTime getLocalDateTime() {
			return localDateTime;
		}

		public void setLocalDateTime(LocalDateTime localDateTime) {
			this.localDateTime = localDateTime;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public String getGrade() {
			return grade;
		}

		public void setGrade(String grade) {
			this.grade = grade;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}
		
}
