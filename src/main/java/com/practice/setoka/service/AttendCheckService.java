package com.practice.setoka.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.setoka.mapper.AttendCheckMapper;

@Service
public class AttendCheckService {
	@Autowired
	private AttendCheckMapper attendCheckMapper;
	
	public void updatePoint(int userNum) {
		attendCheckMapper.updatePoint(userNum);
	}
	public void insertAttendance(int userNum, String date) {
		attendCheckMapper.insertAttendance(userNum, date); 
	}
	public List<String> getAttendanceDate(int UserNum) {
		return attendCheckMapper.getAttendanceDate(UserNum);
	}
}
