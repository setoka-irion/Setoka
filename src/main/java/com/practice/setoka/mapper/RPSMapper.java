package com.practice.setoka.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.practice.setoka.Enum.GameResult;
import com.practice.setoka.Enum.RPSEnum;

@Mapper
public interface RPSMapper {
	public void insertRPSResult(@Param("userNum") int userNum, @Param("choice") RPSEnum choice, @Param("bot") RPSEnum bot,
			@Param("result")GameResult result, @Param("point") int point);
}
