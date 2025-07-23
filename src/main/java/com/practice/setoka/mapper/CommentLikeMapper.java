package com.practice.setoka.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.practice.setoka.dto.CommentLikeDto;

@Mapper
public interface CommentLikeMapper {

		// 좋아요 중복검사
		int existCommentLike(CommentLikeDto likeCommentDto);
			
		// 좋아요 증가기능
		void insertCommentLike(@Param("userNum") int userNum, @Param("commetNum") int commentNum);
			
		//좋아요 취소기능 
		void disLikeComment(CommentLikeDto likeCommentDto);
			
		//	좋아요
		void increaseLikesComment(int commentNum); 
			
		// 좋아요 감소
		void cancelLikesComment(int commentNum);
}
