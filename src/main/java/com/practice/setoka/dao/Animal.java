package com.practice.setoka.dao;

import java.time.LocalDateTime;

public class Animal {
	private int num;
	private int userNum;
	private String animalName;
	private String species;
	private int age;
	private int status;
	private String gender;
	private LocalDateTime togetherDate;
	
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
