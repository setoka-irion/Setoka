package com.practice.setoka.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.practice.setoka.dao.Board;
import com.practice.setoka.dto.BoardDto;
import com.practice.setoka.dto.BoardWithUserDto;
import com.practice.setoka.mapper.BoardMapper;

@Service
@Transactional
public class BoardService {
	
	@Autowired
	private  BoardMapper boardMapper;

	public BoardService(BoardMapper boardMapper) {
		this.boardMapper = boardMapper;
	}

	//	총 게시글 수
	public int countBoards() {
		return boardMapper.countBoards();
	}
	
	//	특정 게시판 전체 리스트
	public List<BoardWithUserDto> findBoardsByType(int type){
		return boardMapper.findBoardsByType(type);
	}
	
	// 	특정 게시글 내용 게시글 넘버로 찾기
	public BoardWithUserDto findBoardByNum(int num){
		return boardMapper.findBoardByNum(num);
	}
	
	// 등록
	public void insertBoard(BoardDto boardDto) {
	    boardMapper.insertBoard(boardDto);
	}

	// 수정
	public void updateBoard(Board board) {
	    boardMapper.updateBoard(board);
	}

	// 삭제
	public void deleteBoard(int num) {
	    boardMapper.deleteBoard(num);
	}

	// 조회수 증가
	public void increaseViewsBoard(int num) {
	    boardMapper.increaseViewsBoard(num);
	}

	// 좋아요 증가
	public void increaseLikesBoard(int num) {
	    boardMapper.increaseLikesBoard(num);
	}

	// 유저 ID로 검색
	public List<BoardWithUserDto> findBoardsByUserId(String id) {
	    return boardMapper.findBoardsByUserId(id);
	}

	// 제목으로 검색
	public List<BoardWithUserDto> findBoardsByTitle(String title) {
	    return boardMapper.findBoardsByTitle(title);
	}

	// 내용으로 검색
	public List<BoardWithUserDto> findBoardsByContent(String content) {
	    return boardMapper.findBoardsByContent(content);
	}

	// 통합 검색
	public List<BoardWithUserDto> searchBoards(Map<String, Object> params) {
	    return boardMapper.searchBoards(params);
	}
}
