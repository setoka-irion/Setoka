package com.practice.setoka.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MemoDto {
	private int userNum;					//유저번호
	private int animalNum;					//동물번호
	private String title;					//제목
	private String content;					//내용
	private LocalDateTime scheduleDate;		//스케줄날짜
	private String scheduleDateStr;
	
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
	
	public String getScheduleDateStr() {
	    return scheduleDateStr;
	}

	public void setScheduleDateStr(String scheduleDateStr) {
	    this.scheduleDateStr = scheduleDateStr;
	}

	@JsonProperty("scheduleDate")
	public String getScheduleDateFormatted() {
		if (scheduleDate == null) return null;
		return scheduleDate.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
	}
	
}
