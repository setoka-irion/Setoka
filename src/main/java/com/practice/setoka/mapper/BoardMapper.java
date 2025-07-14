package com.practice.setoka.mapper;

import java.util.List;
import java.util.Map;

import com.practice.setoka.dao.Board;
import com.practice.setoka.dto.BoardWithUserDto;

public interface BoardMapper {

	//	총 게시글 수 
	int countBoards();
	
	//	특정 게시판 전체리스트
	List<BoardWithUserDto> findBoardsByType(int type);
	 
	//	등록
	void insertBoard(Board board);
	
	//	수정 
	void updateBoard(Board board);
	
	//	삭제
	void deleteBoard(int num);
	
	//	조회수 증가 
	void increaseViewsBoard(int num);
	
	//	좋아요
	void increaseLikesBoard(int num); 
	
	//	싫어요
	void decreaseLikesBoard(int num);
	
	//	유저 아이디로 검색
	List<BoardWithUserDto> findBoardsByUserId(String id);
	
	//	제목으로 검색
	List<BoardWithUserDto> findBoardsByTitle(String title);
	
	//	내용으로 검색
	List<BoardWithUserDto> findBoardsByContent(String content);
	
	//	통합 검색
	List<BoardWithUserDto> searchBoards(Map<String, Object> params);
}
