package com.practice.setoka.mapper;

import java.util.List;

import com.practice.setoka.dao.Comments;
import com.practice.setoka.dto.CommentsInfoDto;

public interface CommentsMapper {
	
	//등록
	void regist(Comments comments);
	
	//수정
	void update(Comments comments);
	
	//삭제
	void delete(Comments comments);
	
	//좋아용
	void addLike(int num);
	
	//해당 게시글 댓글 보기
	List<CommentsInfoDto> findByBoardNum(int boardNum);
}
