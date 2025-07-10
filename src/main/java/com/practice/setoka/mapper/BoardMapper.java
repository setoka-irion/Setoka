package com.practice.setoka.mapper;

import java.util.List;
import java.util.Map;

import com.practice.setoka.dao.Board;
import com.practice.setoka.dto.BoardWithUserDto;

public interface BoardMapper {

	//	총 게시글 수 
	int countAll();
	
	//	특정 게시판 전체리스트
	List<BoardWithUserDto> findBoardsByType(int type);
	 
	//	등록
	void regist(Board board);
	
	//	수정 
	void update(Board board);
	
	//	삭제
	void delete(int num);
	
	//	조회수 증가 
	void addviews (int num);
	
	//	좋아요
	void addlikes (int num); 
	
	//	싫어요
	void minlikes (int num);
	
	//	유저 아이디로 검색
	List<BoardWithUserDto> listbyId(String id);
	
	//	제목으로 검색
	List<BoardWithUserDto> listbytitle(String title);
	
	//	내용으로 검색
	List<BoardWithUserDto> listbycontent(String content);
	
	//	통합 검색
	List<BoardWithUserDto> searchAll(Map<String, Object> params);
}
