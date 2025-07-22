package com.practice.setoka.dao;

import java.time.LocalDateTime;

public class Memo {
	private int num;						//고유번호
	private int userNum;					//유저번호
	private String animalNum;				//동물번호
	private String title;					//제목
	private String content;					//내용
	private LocalDateTime scheduleDate;		//스케줄날짜
	private LocalDateTime registerDate;		//작성일
	
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
	public String getAnimalNum() {
		return animalNum;
	}
	public void setAnimalNum(String animalNum) {
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
	public LocalDateTime getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(LocalDateTime registerDate) {
		this.registerDate = registerDate;
	}
	
	public int[] getIntAnimalNum() {
		if(animalNum == null)
			return null;
		String[] animalNumStr = animalNum.split(",");
		int[] animalNumInt = new int[animalNumStr.length];
		for(int i=0; i<animalNumStr.length;i++) {
			animalNumInt[i] = Integer.parseInt(animalNumStr[i].trim());
		}
		return animalNumInt;
	}
	
}
