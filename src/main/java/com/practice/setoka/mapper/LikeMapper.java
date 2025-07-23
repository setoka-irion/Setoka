package com.practice.setoka.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.practice.setoka.dto.LikeDto;

@Mapper
public interface LikeMapper {

		// 좋아요 중복검사
		int existLike(LikeDto likeDto);
		
		// 좋아요 증가기능
		void insertLike(@Param("userNum") int userNum, @Param("boardNum") int boardNum);
		
		//좋아요 취소기능 
		void disLike(LikeDto likeDto);
		
		//	좋아요
		void increaseLikesBoard(int boardNum); 
		
		// 좋아요 감소
		void cancelLikesBoard(int boardNum);
		
}
