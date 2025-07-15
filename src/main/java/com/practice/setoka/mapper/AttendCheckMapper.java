package com.practice.setoka.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AttendCheckMapper {
	void updatePoint (@Param("num") int userNum);
	
	void insertAttendance(@Param("userNum") int userNum , @Param("date") String date);
	
	List<String> getAttendanceDate(@Param("userNum") int userNum);
}
