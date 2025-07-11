package com.practice.setoka.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.practice.setoka.dao.Board;
import com.practice.setoka.dto.BoardWithUserDto;
import com.practice.setoka.mapper.BoardMapper;

@Service
@Transactional
public class BoardService {

	private final BoardMapper boardMapper;

	public BoardService(BoardMapper boardMapper) {
		this.boardMapper = boardMapper;
	}

	//총 게시글 수
	public int countAll() {
		return boardMapper.countAll();
	}
	
	//등록
	public void regist(Board board) {
		 boardMapper.regist(board);
	}
	
	//수정
	public void update(Board board) {
		boardMapper.update(board);
	}
	
	//삭제
	public void delete (int num) {
		boardMapper.delete(num);
	}
	
	//조회수 증가
	public void addviews (int num) {
		boardMapper.addViews(num);
	}
	
	//좋아요
	public void addlikes (int num) {
		boardMapper.addLikes(num);
	}
	
	//싫어요
	public void minlikes(int num) {
		boardMapper.minLikes(num);
	}
	
	//유저 아이디로 검색
	public List<BoardWithUserDto> listbyId(String id){
		return boardMapper.listById(id);
	}
	
	//제목으로 검색
	public List<BoardWithUserDto> listbytitle(String title){
		return boardMapper.listByTitle(title);
	}
	
	//내용으로 검색
	public List<BoardWithUserDto> listbycontent(String content){
		return boardMapper.listByContent(content);
	}
	
	//통합검색
	public List<BoardWithUserDto> searchAll(Map<String, Object> params){
		return boardMapper.searchAll(params);
	}
}
