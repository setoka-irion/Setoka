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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.practice.setoka.dao.Board;
import com.practice.setoka.dao.Users;
import com.practice.setoka.dto.BoardDto;
import com.practice.setoka.dto.BoardWithUserDto;
import com.practice.setoka.dto.CommentInfoDto;
import com.practice.setoka.service.BoardService;
import com.practice.setoka.service.CommentsService;
import com.practice.setoka.springSecurity.CustomUserDetails;

import jakarta.validation.Valid;

@Controller
public class BoardActionController {

    private final BoardController boardController;

	@Autowired
	public BoardService boardService;
	
	@Autowired
	public CommentsService commentsService;

    BoardActionController(BoardController boardController) {
        this.boardController = boardController;
    }
//	public String test(Model model)
//	{
//		int count = boardService.countBoards();
//		boardService.insertBoard(null);
//		model.addAttribute("count", count);
//		
//		return "";
//	}
	
	//	입양 메인페이지
	@GetMapping(value="/Adopt")
	public String adoptMain(Model model,
			@RequestParam(value="keyword", required = false) String keyword,
	        @RequestParam(value="field", required = false) String field) {
		
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
			case "nickname":
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
	
	
	// 입양 상세 페이지 (조회수증가)
	@GetMapping(value="/AdoptDetail/{num}")
	public String adoptDetail(
			@PathVariable("num")int num,
			@RequestParam(value="editCommentNum", required = false) Integer editCommentNum,
			@AuthenticationPrincipal CustomUserDetails authUser,
			Model model) {
		
		//상세 내용 보여줌
		BoardWithUserDto Detail = boardService.findBoardByNum(num);
		model.addAttribute("detail", Detail);
		
		// 유저에 한해 조회수 증가 
		if(authUser!=null) { //원래 authUser는 비어있기 때문에 아래의 코드가 있으면 무조건 로그인 시킴. 
			Users user = (Users) authUser.getUser();
			if (user !=null) {
				boardService.increaseViewsBoard(num);	
			}
		}
		
		// 해당 게시글 기존 댓글 보여주기
		List<CommentInfoDto> comments = commentsService.findCommentsByBoardNum(num);
		model.addAttribute("comments", comments);		
		
		// 댓글 수정 기능
		if(editCommentNum !=null) {
			CommentInfoDto commentToEdit = commentsService.findCommentByNum(editCommentNum);
			model.addAttribute("commentToEdit",commentToEdit);
		}		
		System.out.println("adoptDetail 진입");
		return "Board/AdoptDetail";
	}
	
	
			 
		
	//입양 게시글 등록 
	@GetMapping(value="/AdoptRegist")
	public String adoptRegistForm(
			@AuthenticationPrincipal CustomUserDetails authUser,
			Model model) {
		//로그인 검증
		Users user = (Users)authUser.getUser(); 
		//세션의 닉네임 저장
		model.addAttribute("writerNick", user.getNickName());
		
		//글 등록시 유저번호 저장
		BoardDto boardDto = new BoardDto();
		boardDto.setUserNum(user.getNum());
		boardDto.setType(1);
		model.addAttribute("boardDto", boardDto);
		return "Board/AdoptRegist";
	}
	
	//입양 게시글 등록
	@PostMapping(value="/AdoptRegist")
	public String adoptRegistSubmit(
			//오류 검증 
			@Valid BoardDto boardDto,
			BindingResult bindingResult, Model model) {
		
		if(bindingResult.hasErrors()) {
			return "Board/AdoptRegist";
		}	
		
		boardService.insertBoard(boardDto);
		return "redirect:/Adopt";
	}
	
	
	
	
	// 입양 게시글 수정
	@GetMapping (value="/AdoptUpdate/{num}")
	public String adoptUpdateForm(
			@PathVariable("num") int num, Model model, 
			@AuthenticationPrincipal CustomUserDetails authUser) {
	
		//해당 게시글 내용
		BoardWithUserDto board = boardService.findBoardByNum(num);  // 상세보기와 동일 코드
	    model.addAttribute("board", board);  // 수정 폼에서 기본값으로 사용
		
		return "Board/AdoptUpdate";
	}
	
	//입양 게시글 수정
	@PostMapping(value="/AdoptUpdate/{num}")
	public String adoptUpdateSubmit(
		@PathVariable("num") int num, Model model,
		@Valid Board board, BindingResult bindingResult, 
		@AuthenticationPrincipal CustomUserDetails authUser,
		RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("num", num); 
		
		//유저 검증
		
		//오류 발생시 다시 수정페이지로
		if(bindingResult.hasErrors()) {

			System.out.println("45634");
			return "/Board/AdoptUpdate";
		}
		System.out.println("123");
		board.setNum(num);
		
		boardService.updateBoard(board);
		return "redirect:/AdoptDetail/" + num;
	}
	
	
	
	//삭제
			@PostMapping("/AdoptDelete/{num}")
			@ResponseBody
			public String adoptDelete(@PathVariable("num") int num, 
						@AuthenticationPrincipal CustomUserDetails authUser,
						RedirectAttributes redirectAttributes) {
				
				// 작성자 정보 가져오기(작성자, 관라자 삭제 권한 확인용) 
				BoardWithUserDto board = boardService.findBoardByNum(num);
				
				// 유저 정보 가져오기 
				Users user = (Users)authUser.getUser();
				
				// 작성자, 관리자 삭제권한 부여
				boolean isAuthur = user.getNum() == board.getUserNum();
				boolean isAdmin = "관리자".equals(user.getGrade()); 
				
				if(! isAuthur && !isAdmin) {
					//redirectAttributes.addFlashAttribute("errorMessage", "작성자만 삭제가능!"); 삭제 경고 메세지
					//return "redirect:/Adopt";
					return "<script>alert('작성자만 삭제할 수 있다는거임!'); history.back();</script>";
					
				}
			
				boardService.deleteBoard(num);
			
				return "<script>location.href='/Adopt';</script>"; //@Responsebody땜에 이거 써야함
			}
			
			

			
			
			//	좋아요	
			@PostMapping("/AdoptDetail/{num}/like")
			public String likeBoard(@PathVariable("num") int num,
						@AuthenticationPrincipal CustomUserDetails authUser) {
				
				//로그인했으면 좋아요
			    if(authUser != null) {
			        boardService.increaseLikesBoard(num);
			    }
			    
			    return "redirect:/AdoptDetail/" + num;
			}
		

			
	//댓글 등록
		@PostMapping(value="AdoptDetail/{num}/comment")
		public String addComment(
				@PathVariable("num") int boardNum, // 게시글 넘버
				@RequestParam("content") String content,// 댓글내용
				@RequestParam(value= "parentNum", defaultValue ="0") int parentNum, //대댓글 기능 없어도 됌
				@AuthenticationPrincipal CustomUserDetails authUser, //로그인 검증용
				Model model) {
		
			//로그인 유저 정보 가져오기용 
			Users user = (Users) authUser.getUser(); 
			
			// 댓글 불러오기
			List<CommentInfoDto> comments = commentsService.findCommentsByBoardNum(boardNum);
			model.addAttribute("comments", comments);
			
			// 댓글 작성
			CommentInfoDto commentInfoDto = new CommentInfoDto();
			commentInfoDto.setUserNum(user.getNum()); 
			commentInfoDto.setBoardNum(boardNum);
			commentInfoDto.setContent(content);
			commentInfoDto.setParentNum(parentNum);	// 대댓글기능이라 없어도 됌
			commentsService.insertComment(commentInfoDto);
					
			
			return "redirect:/AdoptDetail/" + boardNum;
		}
		
		
		//댓글 수정
		@PostMapping("/AdoptDetail/{num}/comment/update")
		public String editComment(
		        @PathVariable("num") int boardNum,
		        @RequestParam("commentNum") int commentNum,
		        @RequestParam("content") String content,
		        @AuthenticationPrincipal CustomUserDetails authUser,
		        RedirectAttributes redirectAttributes) {
			
			//로그인 확인
		    Users user = (Users)authUser.getUser();
		    if (user == null) {
		        redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
		        return "redirect:/AdoptDetail/" + boardNum;
		    }
		    
		    //본인확인
		    CommentInfoDto originalComment = commentsService.findCommentByNum(commentNum);
		    if (originalComment == null || originalComment.getUserNum() != user.getNum()) {
		        redirectAttributes.addFlashAttribute("errorMessage", "본인 댓글만 수정할 수 있습니다.");
		        return "redirect:/AdoptDetail/" + boardNum;
		    }
		    
		    // 댓글 내용불러오기
		    CommentInfoDto commentInfoDto = new CommentInfoDto();
		    commentInfoDto.setNum(commentNum);
		    commentInfoDto.setContent(content);
		    commentInfoDto.setUserNum(user.getNum());

		    commentsService.updateComment(commentInfoDto);
		    
		    //html 에러메세지 보내기
		    redirectAttributes.addFlashAttribute("successMessage", "수정 완료!");
		    return "redirect:/AdoptDetail/" + boardNum;
		}
			
	
		//댓글 삭제
	@PostMapping("/AdoptDetail/{num}/comment/delete")
	@ResponseBody
	public String deleteComment(
			@AuthenticationPrincipal CustomUserDetails authUser,
			@PathVariable("num") int boardNum,
			@RequestParam("commentNum") int commentNum) {
		
		// 현재 로그인한 유저 정보
	    Users loginUser = authUser.getUser();

	    // 댓글 정보 조회 (작성자 확인용)
	    CommentInfoDto comment = commentsService.findCommentByNum(commentNum);

	    // 작성자 본인인지 확인
	    if (comment.getUserNum() != loginUser.getNum()) {
	        // 본인이 아니라면 삭제하지 않고 되돌림 (또는 에러 페이지로)
	        return "<script>alert('작성자만 삭제할 수 있다는 거임!'); location.href='/AdoptDetail/" + boardNum + "';</script>";
	    }
		
		commentsService.deleteComment(commentNum);
		
		return "<script>alert('삭제 완료!'); location.href = '/AdoptDetail/" + boardNum + "';</script>";
	}
	
	//댓글 좋아요
	@PostMapping("/AdoptDetail/{num}/comment/like")
	public String likeComment(
	        @AuthenticationPrincipal CustomUserDetails authUser,
	        @PathVariable("num") int boardNum,
	        @RequestParam("commentNum") int commentNum) {
	    
	    // 로그인 유저 확인
	    Users loginUser = authUser.getUser();
	    if (loginUser == null) {
	        return "redirect:/AdoptDetail/" + boardNum;
	    }

	    // 댓글 존재 확인
	    CommentInfoDto comment = commentsService.findCommentByNum(commentNum);
	    if (comment == null) {
	        return "redirect:/AdoptDetail/" + boardNum;
	    }

	    // 좋아요 증가 처리
	    commentsService.increaseCommentLikes(commentNum);

	    return "redirect:/AdoptDetail/" + boardNum;
	}
	
}
	
	
