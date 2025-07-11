package com.practice.setoka.dto;

import java.time.LocalDateTime;

public class AnimalDto {
	private int userNum;				//유저번호
	private String animalName;			//동물이름
	private String species;				//종
	private int age;					//나이
	private int status;					//상태
	private String gender;				//성별
	private LocalDateTime togetherDate;	//입양일
	
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
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
}
