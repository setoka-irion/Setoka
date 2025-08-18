package com.practice.setoka.dao;

import com.practice.setoka.dto.CarouselItem;

public class Carousel {
	private int num;
	private String img;
	private String link;
	
	public Carousel() {}
	public Carousel(CarouselItem item)
	{
		img = item.getImg();
		link = item.getLink();
	}
	
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
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
