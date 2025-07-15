package com.practice.setoka.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.practice.setoka.dao.Comments;
import com.practice.setoka.dto.CommentInfoDto;

@Mapper
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
	List<CommentInfoDto> findCommentsByBoardNum(int boardNum);
}
