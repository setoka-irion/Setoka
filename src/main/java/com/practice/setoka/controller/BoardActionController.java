package com.practice.setoka.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.practice.setoka.Redirect;
import com.practice.setoka.dao.Users;
import com.practice.setoka.dto.BoardDto;
import com.practice.setoka.dto.BoardWithUserDto;
import com.practice.setoka.service.BoardService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class BoardActionController {

	@Autowired
	public BoardService boardService;
	
//	public String test(Model model)
//	{
//		int count = boardService.countBoards();
//		boardService.insertBoard(null);
//		model.addAttribute("count", count);
//		
//		return "";
//	}
	
	//	입양 메인페이지
//	@GetMapping(value="AdoptMain")
//	public String AdoptMain(Model model,
//			@RequestParam(required = false)String keyword,
//			@RequestParam(required = false)String field) {
//		
//		// 입양 페이지 전체 리스트(좋아요,조회수 포함)
//		List<BoardWithUserDto> findBoardsByType = boardService.findBoardsByType(1);
//		model.addAttribute("typeList", findBoardsByType);
//		
//		// 총 게시글 수
//		int countBoards= boardService.countBoards();
//		model.addAttribute("countBoards", countBoards);
//		
//		// 입양 게시판 내부 검색
//		List<BoardWithUserDto> searchResult;
//		if (keyword == null || keyword.isEmpty()) {
//			searchResult = boardService.findBoardsByType(1);
//		} else { 
//			boardList = boardService.findBoardsByType(countBoards)
//		}
//		
//		model.addAttribute("", searchResult);
//		return "Board/Adopt";
//	}
	
	// 입양 상세 페이지 
	@GetMapping(value="AdoptDetail")
	public String AdoptDetail(@RequestParam("num")int num, Model model) {
		BoardWithUserDto Detail = boardService.findBoardByNum(num);
		model.addAttribute("detail", Detail);
		return "/Board/AdoptDetail";
	}
	
	//입양 글 등록
	@GetMapping(value="AdoptRegist")
	public String AdoptRegistForm(Model model, HttpSession session) {
		//로그인 검증
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		if (user==null) {
			return "redirect:/Login";
		}
		//세션의 닉네임 저장
		model.addAttribute("writerNick", user.getNickName());
		
		//글 등록시 유저번호 저장
		BoardDto boardDto = new BoardDto();
		boardDto.setUserNum(user.getNum());
		model.addAttribute("boardDto", boardDto);
		return "Board/AdoptRegist";
	}
	
	//입양 글 등록
	@PostMapping(value="AdoptRegist")
	public String AdoptRegistSubmit(
			//오류 검증 
			@Valid BoardDto boardDto,
			BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return "Board/AdoptRegist";
		}
		
		boardService.insertBoard(boardDto);
		return "redirect:/Board/Adopt";
	}
	
	// 입양 게시판 수정
	@GetMapping (value="AdoptUpdate")
	public String AdoptUpdateForm(@RequestParam("num") int num, Model model, HttpSession session) {
		
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		if (user==null) {
			return "redirect:/Login";
		}
		//해당 게시글 내용
		BoardWithUserDto board = boardService.findBoardByNum(num);  // 상세보기와 동일 코드
	    model.addAttribute("board", board);  // 수정 폼에서 기본값으로 사용
		
		return "Board/AdoptUpdate";
	}
	
	
	
	// 댓글수
	// 좋아요수
	
	// (로그인시)
	// 입양 게시물 조회수 
	// 입양 게시물 수정
	// 입양 게시물 삭제
	// 입양 게시물 좋아요
	// 입양 상세 게시물 댓글작성
	
	
	
	
//	// 입양 게시판 수정
//	@GetMapping(value="AdoptUpdate")
//	public String updateAdopt(Model model, HttpSession session) {
//		BoardWithUserDto findBoardsByNum = boardService.findBoardsByNum()
//	}
//	
//	@PostMapping(value="AdoptUpdate")
//	public String updateAdopt(Model model, HttpSession session){
//		
//		boardService.updateBoard((Board)model.getAttribute("AdoptData"));
//		return "redirect:/Adopt";
//	}
//	
	
//	조회수 증가 
	
//	좋아요
	
//	싫어요
	
//	유저 아이디로 검색
	
//	제목으로 검색
	
//	내용으로 검색
	
//	통합 검색
	
	
}
