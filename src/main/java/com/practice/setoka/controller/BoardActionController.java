package com.practice.setoka.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.practice.setoka.Redirect;
import com.practice.setoka.dao.Users;
import com.practice.setoka.dto.BoardDto;
import com.practice.setoka.dto.BoardWithUserDto;
import com.practice.setoka.dto.CommentInfoDto;
import com.practice.setoka.service.BoardService;
import com.practice.setoka.service.CommentsService;
import com.practice.setoka.springSecurity.CustomUserDetails;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class BoardActionController {

	@Autowired
	public BoardService boardService;
	
	@Autowired
	public CommentsService commentsService;
//	public String test(Model model)
//	{
//		int count = boardService.countBoards();
//		boardService.insertBoard(null);
//		model.addAttribute("count", count);
//		
//		return "";
//	}
	
	//	입양 메인페이지
	@GetMapping(value="Adopt")
	public String AdoptMain(Model model,
			@RequestParam(required = false)String keyword,
			@RequestParam(required = false)String field) {
		
		//입양게시판 총 게시글 수
		int countBoards= boardService.countBoards(1);
		model.addAttribute("countBoards", countBoards);
		
		// 입양 게시판 내부 검색
		List<BoardWithUserDto> searchResult;
		if (keyword == null || keyword.isEmpty()) {
			//검색 값 안넣었을 경우 전체 게시판 리스트 출력
			searchResult = boardService.findBoardsByType(1);
		} else { 
			switch(field) {
			case "title":
				searchResult = boardService.findBoardsByTitle(keyword.trim());
				break;
			case "content":
				searchResult = boardService.findBoardsByContent(keyword.trim());
				break;
			case "Nickname":
				searchResult = boardService.findBoardsByUserId(keyword.trim());
				break;
			default:
				searchResult = boardService.findBoardsByType(1);
			}
		}
		//메인 리스트 출력
		model.addAttribute("mainList", searchResult);
		// 검색값 유지 (검색창 value 유지용)
		model.addAttribute("keyword", keyword);
		model.addAttribute("field", field);
		
		return "Board/Adopt";
	}
	
	
	
	
	// 입양 상세 페이지 (조회수증가,댓글작성,좋아요기능)
	@GetMapping(value="AdoptDetail")
	public String AdoptDetail(
			@RequestParam("num")int num,
			@RequestParam(value="editCommentNum", required = false) Integer editCommentNum,
			@AuthenticationPrincipal CustomUserDetails authUser,
			Model model) {
		
		//상세 내용 보여줌
		BoardWithUserDto Detail = boardService.findBoardByNum(num);
		model.addAttribute("detail", Detail);
		
		// 유저에 한해 조회수 증가 
		Users user = (Users) authUser.getUser(); 
		if (user !=null) {
			boardService.increaseViewsBoard(num);	
		}
		
		// 해당 게시글 기존 댓글 보여주기
		List<CommentInfoDto> comments = commentsService.findCommentsByBoardNum(num);
		model.addAttribute("comments", comments);		
		
		// 댓글 수정 기능
		if(editCommentNum !=null) {
			CommentInfoDto commentToEdit = commentsService.findCommentByNum(editCommentNum);
			model.addAttribute("commentToEdit",commentToEdit);
		}		
		return "/Board/AdoptDetail";
	}
	
	
			 
		
	//입양 글 등록 (조회수증가,댓글작성기능)
	@GetMapping(value="AdoptRegist")
	public String AdoptRegistForm(
			@AuthenticationPrincipal CustomUserDetails authUser,
			Model model) {
		//로그인 검증
		Users user = (Users)authUser.getUser(); 
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
	@GetMapping (value="/AdoptUpdate/{num}")
	public String AdoptUpdateForm(
			@PathVariable("num") int num, Model model, 
			@AuthenticationPrincipal CustomUserDetails authUser) {
		
	
		//해당 게시글 내용
		BoardWithUserDto board = boardService.findBoardByNum(num);  // 상세보기와 동일 코드
	    model.addAttribute("board", board);  // 수정 폼에서 기본값으로 사용
		
		return "Board/AdoptUpdate";
	}
	
	//입양 게시판 수정
	@PostMapping(value="AdoptUpdate/{num}")
	public String AdoptUpdateSubmit(
		@Valid BoardDto boardDto, BindingResult bindingResult, 
		@AuthenticationPrincipal CustomUserDetails authUser,
		@PathVariable("num") int num, Model model) {
		
		
		//유저 검증
		
		//오류 발생시 다시 수정페이지로
		if(bindingResult.hasErrors()) {
			return "Board/AdoptUpdate";
		}
		
		boardDto.setNum(num);
		boardService.updateBoard(boardDto);
		return "redirect:/Board/AdoptDetail/"+num;
	}
	
	
	
	
	//댓글 기능
		@PostMapping(value="AdoptDetail/{num}/comment")
		public String AddComment(
				@PathVariable("num") int boardNum, // 게시글 넘버
				@RequestParam("content") String content,// 댓글내용
				@RequestParam(value= "parentNum", defaultValue ="0") int parentNum, 
				@AuthenticationPrincipal CustomUserDetails authUser, //로그인 검증용
				Model model, CommentInfoDto commentInfoDto) {
			
			
			
			//로그인 유저 정보 가져오기용 
			Users user = (Users) authUser.getUser(); 
			
			// 댓글 작성
			commentInfoDto.setUserNum(user.getNum()); //닉네임 들어가야하는거 아닌가.
			commentInfoDto.setBoardNum(boardNum);
			commentInfoDto.setContent(content);
			commentInfoDto.setParentNum(parentNum);	// 댓글작성하는데 이게 필요한가?
			
			commentsService.insertComment(commentInfoDto);
					
			//댓글 삭제
			return "redirect:/Board/AdoptDetail?num=" + boardNum;
		}
		
		//댓글 수정
		@PostMapping ("/AdoptDetail/{num}/comment/edit")
		public String editComment(
				@PathVariable("num") int boardNum,
				@RequestParam("commentNum") int commentNum,
				@RequestParam("content") String content,
				@AuthenticationPrincipal CustomUserDetails authUser,
				CommentInfoDto commentInfoDto) {
			
			Users user = (Users)authUser.getUser(); 
				
			commentInfoDto.setNum(commentNum);
			commentInfoDto.setContent(content);
			commentInfoDto.setUserNum(user.getNum());
			
			commentsService.updateComment(commentInfoDto);
			return "redirect:/Board/AdoptDetail?num=" + boardNum;
		}
	
		//삭제
		@PostMapping("/AdoptDelete")
		public String adoptDelete(@RequestParam("num") int num, 
					@AuthenticationPrincipal CustomUserDetails authUser) {
			
			// 작성자 정보 가져오기(작성자, 관라자 삭제 권한 확인용) 
			BoardWithUserDto board = boardService.findBoardByNum(num);
			
			// 유저 정보 가져오기 
			Users user = (Users)authUser.getUser();
			
			// 작성자, 관리자 삭제권한 부여
			boolean isAuthur = user.getNum() == board.getUserNum();
			boolean isAdmin = "관리자".equals(user.getGrade()); 
			if(! isAuthur || isAdmin) {
				return "/Board/adopt";
				
			}
		
			boardService.deleteBoard(num);
		
			return "redirect:/Board/Adopt";
		}
			
		//	좋아요	
		@PostMapping("/AdoptDetail/{num}/like")
		public String likeBoard(@PathVariable("num") int num,
					@AuthenticationPrincipal CustomUserDetails authUser) {
			//로그인했으면 좋아요
		    if(authUser != null) {
		        boardService.increaseLikesBoard(num);
		    }
		    return "redirect:/Board/AdoptDetail?num=" + num;
		}
	}
