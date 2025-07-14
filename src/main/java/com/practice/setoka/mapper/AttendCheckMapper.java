package com.practice.setoka.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AttendCheckMapper {
	void updatePoint (@Param("userNum") int userNum) ;
}
