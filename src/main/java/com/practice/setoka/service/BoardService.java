package com.practice.setoka.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.practice.setoka.Upload;
import com.practice.setoka.dao.Report;
import com.practice.setoka.dao.TempImage;
import com.practice.setoka.dto.BoardDto;
import com.practice.setoka.dto.BoardWithUserDto;
import com.practice.setoka.dto.TempImageDto;
import com.practice.setoka.mapper.BoardMapper;

@Service
@Transactional
public class BoardService {
	
	@Autowired
	private  BoardMapper boardMapper;
	@Autowired
	private Upload upload;

	public BoardService(BoardMapper boardMapper) {
		this.boardMapper = boardMapper;
	}

	//	총 게시글 수
	public int countBoards(int num) {
		return boardMapper.countBoards(num);
	}
	
	//	특정 게시판 전체 리스트
	public List<BoardWithUserDto> findBoardsByType(int type, int offset, int limit) {
	    Map<String, Object> params = new HashMap<>();
	    params.put("type", type);
	    params.put("offset", offset);
	    params.put("limit", limit);
	    return boardMapper.findBoardsByType(params);
	}
	
	// 	특정 게시글 내용 게시글 넘버로 찾기
	public BoardWithUserDto findBoardByNum(int num){
		return boardMapper.findBoardByNum(num);
	}
	
	// 등록
	public void insertBoard(BoardDto boardDto) {
		System.out.println(boardDto.getImage_paths());
		if(boardDto.getImage_paths() == null || boardDto.getImage_paths().length() == 0)
		{
			boardDto.setImage_paths(upload.imagePath + "defaultBoard.png");
		}
	    boardMapper.insertBoard(boardDto);
	}

	// 수정
	public void updateBoard(BoardDto boardDto, int num) {	
		if(boardDto.getImage_paths() == null || boardDto.getImage_paths().length() == 0)
		{
			boardDto.setImage_paths(upload.imagePath + "defaultBoard.png");
		}
	    boardMapper.updateBoard(boardDto, num);
	}

	// 삭제
	public void deleteBoard(int num) {
	    boardMapper.deleteBoard(num);
	}

	// 조회수 증가
	public void increaseViewsBoard(int num) {
	    boardMapper.increaseViewsBoard(num);
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
	public List<BoardWithUserDto> searchAll(String keyword) {
	    return boardMapper.searchAll(keyword);
	}
	
	//신고 
	public void reportBoard(int boardNum, int userNum) {
		boardMapper.reportBoard(boardNum, userNum);
	}
	
	// 신고 
	
	// 게시판 신고 유저 내용
	public Report findReportByBC (int boardNum, int userNum) {
		return boardMapper.findReportByBC(boardNum, userNum);
	}
	
	// 인기 게시글
	public List<BoardWithUserDto> popularPosts(int type){
		return boardMapper.popularPosts(type);
	}

	public void insertTempImage(TempImageDto dto)
	{
		boardMapper.insertTempImage(dto);
	}
	
	public List<TempImage> selectTempImageAllToUsersNum(int userNum)
	{
		return boardMapper.selectTempImageAllToUsersNum(userNum);
	}
	
	public void DeleteTempImage(int userNum)
	{
		boardMapper.deleteTempImage(userNum);
	}
	
	public List<BoardWithUserDto> reportBoardList(int count)
	{
		return boardMapper.reportBoardSelect(count);
	}
	
	//페이지 자르는 메소드						시작점		몇개를 자를건지		전체 리스트
	public List<BoardWithUserDto> cutPage (int offset, int limit, List<BoardWithUserDto>cutPage){
		//리턴해줄 리스트 생성 (지금은 비어있음)
		List<BoardWithUserDto> result = new ArrayList<BoardWithUserDto>();
		//시작점이 전체 리스트보다 큰 경우 (시작점이 40인데 리스트에 30개 밖에 없는 경우)
		if ( offset > cutPage.size())
		{
			//만들어둔 리스트 리턴 (지금 시점에는 비어있음)
			return result;
		}
		
		//반복문 (시작점애서 + limit 까지)
		for (int i = offset; i < offset + limit; i++) {
			//현재 넣어야 할 위치가 전체 리스트 보다 커짐 (30에서 시작해서 36번째 넣으러는데 전체 리스트가 35개인 경우)
			if (cutPage.size() <= i)
				//현재 까지 넣은 리스트 리턴
				return result;
			//리스트에 현재 페이지 추가
			result.add(cutPage.get(i));
		}
		
		//리스트 리턴
		return result;  
	}
}
