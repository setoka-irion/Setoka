package com.practice.setoka.dto;

public class TempImageDto {
	private int userNum;
	private String imageName;
	
	public TempImageDto() {}
	public TempImageDto(int userNum, String imageName)
	{
		this.userNum = userNum;
		this.imageName = imageName;
	}
	
	public int getUserNum() {
		return userNum;
	}
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
}
