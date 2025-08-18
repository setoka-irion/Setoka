package com.practice.setoka.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BoardWithUserDto {
	
	//게시글 
	private int boardNum;
	//유저 정보
	private int userNum;
	private String userId;
	private String nickName;
	private String realName;
	private String grade;
	//게시글 정보
	private String title;
	private String content;
	private byte type;
	private int likes;  
	private int views;
	private int price;
	private String area;
	private LocalDateTime registerDate;
	private String Image_paths;
	//인기 게시글용 댓글 수 
	private int commentCount;
	
	
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public int getBoardNum() {
		return boardNum;
	}
	public void setBoardNum(int boardNum) {
		this.boardNum = boardNum;
	}
	public int getUserNum() {
		return userNum;
	}
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public int getViews() {
		return views;
	}
	public void setViews(int views) {
		this.views = views;
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
	public LocalDateTime getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(LocalDateTime registerDate) {
		this.registerDate = registerDate;
	}	
	
	public String getImage_paths() {
		return Image_paths;
	}
	public void setImage_paths(String image_paths) {
		Image_paths = image_paths;
	}
	public String getFormattedRegistDate() {
		if (registerDate == null)
			return "";
		LocalDate registLocalDate = registerDate.toLocalDate();
		LocalDate today = LocalDate.now();

		if (registLocalDate.equals(today)) {
			return registerDate.format(DateTimeFormatter.ofPattern("HH:mm"));
		} else {
			return registerDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
		}
	}

	public String getFormattedRegistDateOne() {
		if (registerDate == null)
			return "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
		return registerDate.format(formatter);
	}

	public String getDetailUrl() {
	    String path = switch (this.type) {
	        case 1 -> "/AdoptDetail/";
	        case 2 -> "/AnimalPrideDetail/";
	        case 3 -> "/WalkTrailDetail/";
	        case 4 -> "/UsedGoodsDetail/";
	        case 5 -> "/KnowhowDetail/";
	        default -> "/Unknown/";
	    };
	    return path + this.boardNum;
	}
}
