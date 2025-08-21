package com.practice.setoka.dao;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.practice.setoka.RatingRule;
import com.practice.setoka.Enum.Grade;
import com.practice.setoka.Enum.Privileges;
import com.practice.setoka.Enum.Status;
import com.practice.setoka.dto.UsersDto;

public class Users implements Serializable{
	private int num;
	private String id;
	private String password;
	private String nickName;
	private String realName;
	private String phoneNumber;
	private Status status;
	private int point;
	private Privileges privileges;
	private long exp;
	private String profilePath;
	private LocalDateTime registerDate;
	private Grade grade;
	

	public void modifyUser(UsersDto userDto) {
		nickName = userDto.getNickName();
		realName = userDto.getRealName();
		phoneNumber = userDto.getPhoneNumber();
	}
	
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
	public String getProfilePath() {
		return profilePath;
	}
	public void setProfilePath(String profilePath) {
		this.profilePath = profilePath;
	}
	public LocalDateTime getRegisterDate() { return registerDate; }
	public void setRegisterDate(LocalDateTime registerDate) { this.registerDate = registerDate; }

	public Privileges getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Privileges privileges) {
		this.privileges = privileges;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public Grade getGrade() {
		grade = RatingRule.ratingRule.GetGrade(exp);
		return grade;
	}

	public void PlusPoint(int point)
	{
		this.point += point;
		exp += point;
	}
	public boolean isAdmin()
	{
		return privileges.equals(Privileges.관리자);
	}
}
