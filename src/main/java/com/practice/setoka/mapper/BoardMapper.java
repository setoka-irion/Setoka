package com.practice.setoka.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.practice.setoka.dao.Board;
import com.practice.setoka.dao.Report;
import com.practice.setoka.dto.BoardDto;
import com.practice.setoka.dto.BoardWithUserDto;

@Mapper
public interface BoardMapper {

	//	총 게시글 수 
	int countBoards(int num);
	
	//	특정 게시판 전체리스트
	List<BoardWithUserDto> findBoardsByType(int type);
	
	// 	특정 게시글 내용 게시글 넘버로 찾기(리스트 아님)
	BoardWithUserDto findBoardByNum(@Param("num") int num);
	
	//	등록
	void insertBoard(BoardDto boardDto);
	
	//	수정 
	void updateBoard(Board board);
	
	//	삭제
	void deleteBoard(int num);
	
	//	조회수 증가 
	void increaseViewsBoard(int num);
	
	//	유저 아이디로 검색
	List<BoardWithUserDto> findBoardsByUserId(String id);
	
	//	제목으로 검색
	List<BoardWithUserDto> findBoardsByTitle(String title);
	
	//	내용으로 검색
	List<BoardWithUserDto> findBoardsByContent(String content);
	
	//	통합 검색
	List<BoardWithUserDto> searchAll(String keyword);
	
	// 신고 
	void reportBoard(@Param("boardNum")int boardNum,@Param("userNum") int userNum);
	
//	// 신고 내용
//	List<Report> findReportByBoardNum(@Param("boardNum") int boardNum);
//	
	// 게시글 신고 유저내용
	Report findReportByBC(@Param("boardNum")int boardNum,@Param("userNum") int userNum);
	
}
