package com.practice.setoka.mapper;

import java.util.List;

import com.practice.setoka.dao.Comments;
import com.practice.setoka.dto.CommentsInfoDto;

public interface CommentsMapper {
	
	//등록
	void insertComment(Comments comments);
	
	//수정
	void updateComment(Comments comments);
	
	//삭제
	void deleteComment(int num);
	
	//좋아용
	void increaseCommentLikes(int num);
	
	//해당 게시글 댓글 보기
	List<CommentsInfoDto> findCommentsByBoardNum(int boardNum);
}
