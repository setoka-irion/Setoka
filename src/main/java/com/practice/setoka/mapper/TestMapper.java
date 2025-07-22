package com.practice.setoka.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TestMapper {
	int insertContent(@Param("content") String content);
	
	String selectContent(@Param("num") int num);
}
