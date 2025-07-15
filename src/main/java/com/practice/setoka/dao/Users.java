package com.practice.setoka.dao;

import java.time.LocalDateTime;

import com.practice.setoka.Enum.Grade;
import com.practice.setoka.Enum.Status;

public class Users {
	private int num;
	private String id;
	private String password;
	private String nickName;
	private String realName;
	private String phoneNumber;
	private Status status;
	private int point;
	private Grade grade = Grade.브론즈;;
	private LocalDateTime registerDate;
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public Grade getGrade() {
		return grade;
	}
	public void setGrade(Grade grade) {
		this.grade = grade;
	}
	public LocalDateTime getRegisterDate() { return registerDate; }
	public void setRegisterDate(LocalDateTime registerDate) { this.registerDate = registerDate; }
	
	public boolean isAdmin()
	{
		return status.equals("admin");
	}
}
