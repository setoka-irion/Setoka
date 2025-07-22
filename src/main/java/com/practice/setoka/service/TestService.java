package com.practice.setoka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.setoka.mapper.TestMapper;

@Service
public class TestService {
	@Autowired
	TestMapper testmapper;
	
	public int insertContent(String content) {
		return testmapper.insertContent(content);
	}
	
	public String selectContent(int num) {
		return testmapper.selectContent(num);
	}

}
