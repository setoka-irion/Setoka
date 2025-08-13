package com.practice.setoka.dto;

public class CarouselItem {
	private String img;
	private String link;
	
	public CarouselItem() {}
	public CarouselItem(String img, String link)
	{
		this.img = img;
		this.link = link;
	}
	
	
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
}
