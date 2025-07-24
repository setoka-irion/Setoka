package com.practice.setoka.dao;

import java.time.LocalDateTime;

public class Report {
		
	private int num;
	private int userNum;
	//boardNum 이자 commentNum일수 있다
	private int bcNum;
	private LocalDateTime reported;
	
	
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
	public int getBcNum() {
		return bcNum;
	}
	public void setBcNum(int bcNum) {
		this.bcNum = bcNum;
	}
	public LocalDateTime getReported() {
		return reported;
	}
	public void setReported(LocalDateTime reported) {
		this.reported = reported;
	}
	
}
