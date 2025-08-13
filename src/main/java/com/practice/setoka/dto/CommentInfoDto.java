package com.practice.setoka.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentInfoDto {
		
	//comment
		int num;
		int userNum;
		int parentNum;
		int boardNum;
		String content;
		int likes;
		private LocalDateTime registerDate ;
		int status;
		
	//users
		String nickname;
		String userGrade;
		String profilePath;
	//board
		int type;

		
		public String getProfilePath() {
			return profilePath;
		}

		public void setProfilePath(String profilePath) {
			this.profilePath = profilePath;
		}

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

		public LocalDateTime getRegisterDate() {
			return registerDate;
		}

		public void setRegisterDate(LocalDateTime registerDate) {
			this.registerDate = registerDate;
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

		public String getUserGrade() {
			return userGrade;
		}

		public void setUserGrade(String userGrade) {
			this.userGrade = userGrade;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}
		public String getFormattedRegistDate() {
			if (registerDate == null)
				return "";
			LocalDate registLocalDate = registerDate.toLocalDate();
			LocalDate today = LocalDate.now();

			if (registLocalDate.equals(today)) {
				return registerDate.format(DateTimeFormatter.ofPattern("HH:mm"));
			} else {
				return registerDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
			}
		}

		public String getFormattedRegistDateOne() {
			if (registerDate == null)
				return "";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
			return registerDate.format(formatter);
		}
}
