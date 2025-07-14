package com.practice.setoka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.setoka.mapper.AttendCheckMapper;

@Service
public class AttendCheckService {
	@Autowired
	private AttendCheckMapper attendCheckMapper;
	
	private void updatePoint(int userNum) {
		attendCheckMapper.updatePoint(userNum);
	}
}
