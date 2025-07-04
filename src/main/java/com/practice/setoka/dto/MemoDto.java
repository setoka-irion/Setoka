package com.practice.setoka.dto;

import java.time.LocalDateTime;

public class MemoDto {
	private int userNum;
	private int animalNum;
	private String title;
	private String content;
	private LocalDateTime scheduleDate;

	public int getUserNum() {
		return userNum;
	}

	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}

	public int getAnimalNum() {
		return animalNum;
	}

	public void setAnimalNum(int animalNum) {
		this.animalNum = animalNum;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(LocalDateTime scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

}
