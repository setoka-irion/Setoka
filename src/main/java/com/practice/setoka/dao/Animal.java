package com.practice.setoka.dao;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.practice.setoka.Enum.AnimalStatus;

public class Animal {
	private int num;						//고유번호
	private int userNum;					//유저번호
	private String animalName;				//동물번호
	private String species;					//종
	private int age;						//나이
	private AnimalStatus status;			//상태
	private String gender;					//성별
	private LocalDateTime togetherDate;		//입양일
	private String profilePath;				//프로필사진
	
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
	public String getAnimalName() {
		return animalName;
	}
	public void setAnimalName(String animalName) {
		this.animalName = animalName;
	}
	public String getSpecies() {
		return species;
	}
	public void setSpecies(String species) {
		this.species = species;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public AnimalStatus getStatus() {
		return status;
	}
	public void setStatus(AnimalStatus status) {
		this.status = status;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public LocalDateTime getTogetherDate() {
		return togetherDate;
	}
	public void setTogetherDate(LocalDateTime togetherDate) {
		this.togetherDate = togetherDate;
	}
	public String getProfilePath() {
		return profilePath;
	}
	public void setProfilePath(String profilePath) {
		this.profilePath = profilePath;
	}
	public long getTogetherDateLong() {
		LocalDateTime now = LocalDateTime.now();
		return ChronoUnit.DAYS.between(togetherDate, now);
	}
	
}
