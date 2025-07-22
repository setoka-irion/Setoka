package com.practice.setoka.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.practice.setoka.dao.Comments;
import com.practice.setoka.dto.CommentInfoDto;

@Mapper
public interface CommentsMapper {
	
	//등록
	void insertComment(CommentInfoDto commentInfoDto);
	
	//수정
	void updateComment(CommentInfoDto commentInfoDto);
	
	//삭제
	void deleteComment(int num);
	
	//댓글 좋아용
	void increaseCommentLikes(int num);
	
	//해당 게시글 댓글 보기
	List<CommentInfoDto> findCommentsByBoardNum(@Param("boardNum")int boardNum);
	
	// 댓글 수정용 해당 댓글 하나 불러오기 
	CommentInfoDto findCommentByNum(int num); 
}
