package com.practice.setoka.dto;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BoardDto {
	
    @NotNull(message = "작성자 정보가 누락되었습니다.")
	private int userNum;
	@NotBlank(message = "제목은 필수입니다.")
	private String title;
	@NotBlank(message = "내용은 필수입니다.")
	private String  content;
	private Integer type;
	private int price;
	private String area;
	private String Image_paths;
	
	
	public int getUserNum() {
		return userNum;
	}
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String  getContent() {
		return content;
	}
	public void setContent(String  content) {
		this.content = content;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getImage_paths() {
		return Image_paths;
	}
	public void setImage_paths(String image_paths) {
		Image_paths = image_paths;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
}
