package com.practice.setoka.dao;

import java.time.LocalDateTime;

import com.practice.setoka.Enum.Item;
import com.practice.setoka.Enum.MessageStatus;

public class Message {
	private int num;
	private String sender;
	private String receiver;
	private String title;
	private String content;
	private Item item_Type;
	private int item_Value;
	private MessageStatus messageStatus;
	private LocalDateTime sendTime;
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
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
	public Item getItem_Type() {
		return item_Type;
	}
	public void setItem_Type(Item item_Type) {
		this.item_Type = item_Type;
	}
	public int getItem_Value() {
		return item_Value;
	}
	public void setItem_Value(int item_Value) {
		this.item_Value = item_Value;
	}
	public MessageStatus getMessageStatus() {
		return messageStatus;
	}
	public void setMessageStatus(MessageStatus messageStatus) {
		this.messageStatus = messageStatus;
	}
	public LocalDateTime getSendTime() {
		return sendTime;
	}
	public void setSendTime(LocalDateTime sendTime) {
		this.sendTime = sendTime;
	}
	
	
}
