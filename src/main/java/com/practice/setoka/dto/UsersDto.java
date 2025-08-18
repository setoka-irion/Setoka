package com.practice.setoka.dto;

import com.practice.setoka.RatingRule;
import com.practice.setoka.Enum.Grade;
import com.practice.setoka.Enum.Privileges;
import com.practice.setoka.Enum.Status;
import com.practice.setoka.dao.Users;

public class UsersDto {
	private String id;
	private String password;
	private String nickName;
	private String realName;
	private String phoneNumber;
	private Status status;
	private int point;
	private Privileges privileges = Privileges.일반인;
	private long exp;
	private String profilePath;
	private Grade grade = Grade.브론즈;
	
	public UsersDto() {}
	public UsersDto(Users user) {
		id = user.getId();
		password = user.getPassword();
		nickName = user.getNickName();
		realName = user.getRealName();
		phoneNumber = user.getPhoneNumber();
		status = user.getStatus();
		point = user.getPoint();
		privileges = user.getPrivileges();
		exp = user.getExp();
		profilePath = user.getProfilePath();
		grade = user.getGrade();
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
	
	public long getExp() {
		return exp;
	}
	public void setExp(long exp) {
		this.exp = exp;
	}
	public Privileges getPrivileges() {
		return privileges;
	}
	public void setPrivileges(Privileges privileges) {
		this.privileges = privileges;
	}
	public Grade getGrade() {
		grade = RatingRule.ratingRule.GetGrade(exp);
		return grade;
	}
}
