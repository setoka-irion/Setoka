package com.practice.setoka.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.practice.setoka.Upload;
import com.practice.setoka.dao.Board;
import com.practice.setoka.dao.TempImage;
import com.practice.setoka.dao.Users;
import com.practice.setoka.dto.BoardDto;
import com.practice.setoka.dto.BoardWithUserDto;
import com.practice.setoka.dto.CommentInfoDto;
import com.practice.setoka.dto.CommentLikeDto;
import com.practice.setoka.dto.LikeDto;
import com.practice.setoka.service.BoardService;
import com.practice.setoka.service.CommentLikeService;
import com.practice.setoka.service.CommentsService;
import com.practice.setoka.service.LikeService;
import com.practice.setoka.springSecurity.CustomUserDetails;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class BoardActionController {

	@Autowired
	private final Upload upload;

	private final BoardController boardController;

	@Autowired
	public BoardService boardService;

	@Autowired
	public CommentsService commentsService;

	@Autowired
	public LikeService likeService;

	@Autowired
	public CommentLikeService commentLikeService;

	BoardActionController(BoardController boardController, Upload upload) {
		this.boardController = boardController;
		this.upload = upload;
	}
//	public String test(Model model)
//	{
//		int count = boardService.countBoards();
//		boardService.insertBoard(null);
//		model.addAttribute("count", count);
//		
//		return "";
//	}

	// 입양 메인페이지
	@GetMapping(value = "/Adopt")
	public String adoptMain(Model model, 
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "field", required = false) String field, 
			@RequestParam(value = "page", defaultValue = "1") int page) {

		// 페이지 네이션 기능
		int limit = 15; //한페이지당 게시글수
		int offset = (page - 1) * limit; 
		int totalCount;
		
		// 입양 게시판 내부 검색
		List<BoardWithUserDto> searchResult;
		if (keyword == null || keyword.isEmpty()) {
			// 검색 값 안넣었을 경우 전체 게시판 리스트 출력
			searchResult = boardService.findBoardsByType(1, offset, limit); // findBoardsByType 댓글수 때문에 바꿈
			totalCount = boardService.countBoards(1);
		} else {
			switch (field) {
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
				searchResult = boardService.searchAll(keyword.trim());
			}
			totalCount = searchResult.size(); //결색결과 갯수
		}
		
		// Adopt 인기게시글
		List<BoardWithUserDto> popularPosts = boardService.popularPosts(1);
		searchResult = boardService.cutPage(offset, limit, searchResult);
		// 페이지네이션 기능용

		
		int totalPages = (int) Math.ceil((double) totalCount / limit);	// 총게시글수/페이지당 갯수
		model.addAttribute("totalPages", totalPages);
		
		// 썸네일 이미지 없으면 기본 이미지 적용
		for(var dto : searchResult)
		{
			if(dto.getImage_paths() == null)
			{
				dto.setImage_paths("default.jpg");
			}
		}
		
		//인기글
		model.addAttribute("popularPosts", popularPosts);
		// 메인 리스트 출력 + 페이지 네이션 기능
		model.addAttribute("mainList", searchResult);
		model.addAttribute("limit", limit);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("currentPage", page);
		
		// 검색값 유지 (검색창 value 유지용)
		model.addAttribute("keyword", keyword);
		model.addAttribute("field", field);
		return "Board/Adopt";
	}

	// 입양 상세 페이지 (조회수증가)
	@GetMapping(value = "/AdoptDetail/{num}")
	public String adoptDetail(@PathVariable("num") int num,
			@RequestParam(value = "editCommentNum", required = false) Integer editCommentNum,
			@AuthenticationPrincipal CustomUserDetails authUser, HttpSession session, Model model) {

		// 수정 삭제버튼 감추기용

		// 상세 내용 보여줌
		BoardWithUserDto detail = boardService.findBoardByNum(num);
		String content = upload.fileLoad(detail.getContent());
		detail.setContent(content);
		
		
		model.addAttribute("detail", detail);

		// 세션에서 조회한 게시글 번호 리스트 받아오기, 없으면 만듦(조회수증가기능)
		@SuppressWarnings("unchecked")
		List<Integer> viewedBoards = (List<Integer>) session.getAttribute("VIEWED_BOARDS");
		if (viewedBoards == null) {
			viewedBoards = new ArrayList<>();
		}

		// 유저에 한해 조회수 증가
		if (authUser != null) { // 원래 authUser는 비어있기 때문에 아래의 코드가 있으면 무조건 로그인 시킴.
			Users user = (Users) authUser.getUser();
			if (user != null) {
				// 이 게시글 번호가 세션에 없으면 조회수 증가 및 세션에 저장
				if (!viewedBoards.contains(num)) {
					boardService.increaseViewsBoard(num);
					viewedBoards.add(num);
					session.setAttribute("VIEWED_BOARDS", viewedBoards);
				}
			}
		}
		// 해당 게시글 기존 댓글 보여주기
		List<CommentInfoDto> comments = commentsService.findCommentsByBoardNum(num);
		model.addAttribute("comments", comments);

		// 댓글 수정 기능
		if (editCommentNum != null) {
			CommentInfoDto commentToEdit = commentsService.findCommentByNum(editCommentNum);
			model.addAttribute("commentToEdit", commentToEdit);
		}

		System.out.println("adoptDetail 진입");
		return "Board/AdoptDetail";
	}

	// 입양 게시글 등록
	@GetMapping(value = "/AdoptRegist")
	public String adoptRegistForm(@AuthenticationPrincipal CustomUserDetails authUser, Model model) {
		// 로그인 검증
		Users user = (Users) authUser.getUser();
		// 세션의 닉네임 저장
		model.addAttribute("writerNick", user.getNickName());

		// 글 등록시 유저번호 저장
		BoardDto boardDto = new BoardDto();
		boardDto.setUserNum(user.getNum());
		boardDto.setType(1);
		model.addAttribute("boardDto", boardDto);
		
		//예비 db 비우기
		boardService.DeleteTempImage(user.getNum());
		
		return "Board/AdoptRegist";
	}

	// 입양 게시글 등록
	@PostMapping(value = "/AdoptRegist")
	public String adoptRegistSubmit(
			// 오류 검증
			@Valid BoardDto boardDto,
			@RequestParam("images") List<MultipartFile> images, BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {
			return "Board/AdoptRegist";
		}
		String original=boardDto.getContent().replaceAll("images/temp/", "images/");
		boardDto.setContent(original);
		
		String fileName = upload.fileUpload(boardDto.getContent());
		String thumnailName = null;
		if(images != null && images.size() > 0)
			thumnailName = upload.imageFileUpload(images.get(0));
		boardDto.setContent(fileName);
		boardDto.setImage_paths(thumnailName);
		boardService.insertBoard(boardDto);
		
		// 예비 db에서 가져오기
		List<TempImage> list = boardService.selectTempImageAllToUsersNum(boardDto.getUserNum());
		// 제대로 된 db로 옮기기
		for (TempImage l : list) {
			TempImage tempImage = l;
			
			// 원본 파일 경로
			Path sourcePath = Paths.get("C:/images/temp/" + tempImage.getImageName());

			// 이동할 대상 경로
			Path targetPath = Paths.get("C:/images/" + tempImage.getImageName());

			try {
				// 파일 이동 (이미 존재하면 덮어쓰기)
				Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("파일이 성공적으로 이동되었습니다!");
			} catch (Exception e) {
				System.err.println("파일 이동 중 오류 발생: " + e.getMessage());
			}
		}
		
		return "redirect:/Adopt";
	}

	// 입양 게시글 수정
	@GetMapping(value = "/AdoptUpdate/{num}")
	public String adoptUpdateForm(@PathVariable("num") int num, Model model,
			@AuthenticationPrincipal CustomUserDetails authUser, RedirectAttributes redirectAttributes) {
		// 작성자 정보 가져오기(작성자, 관라자 삭제 권한 확인용)
		BoardWithUserDto board = boardService.findBoardByNum(num);

		// 유저 정보 가져오기
		Users user = (Users) authUser.getUser();

		// 작성자, 관리자 삭제권한 부여
		boolean isAuthur = user.getNum() == board.getUserNum();
		boolean isAdmin = "관리자".equals(user.getGrade());

		if (!isAuthur && !isAdmin) {
			redirectAttributes.addFlashAttribute("errorMessage", "작성자만 수정가능!");
			return "redirect:/AdoptDetail/" + num;
		}
		// 해당 게시글 내용
		// 상세보기와 동일 코드
		board.setContent(upload.fileLoad(board.getContent()));
		model.addAttribute("board", board); // 수정 폼에서 기본값으로 사용

		return "Board/AdoptUpdate";
	}

	// 입양 게시글 수정
	@PostMapping(value = "/AdoptUpdate/{num}")
	public String adoptUpdateSubmit(@PathVariable("num") int num, Model model, @Valid Board board,
			BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetails authUser,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("num", num);

		// 유저 검증
		Users user = (Users) authUser.getUser();

		// 수정전 작성자 내용불러오기
		BoardWithUserDto existing = boardService.findBoardByNum(num);
		boolean isAuthor = user.getNum() == existing.getUserNum();
		boolean isAdmin = "관리자".equals(user.getGrade());

		if (!isAuthor && !isAdmin) {
			redirectAttributes.addFlashAttribute("errorMessage", "권한이 없습니다.");
			return "redirect:/AdoptDetial" + num;
		}

		// 오류 발생시 다시 수정페이지로
		if (bindingResult.hasErrors()) {
			model.addAttribute("board", board); // 반드시 넣어줘야 함

			return "/Board/AdoptUpdate";
		}

		// 텍스트, 사진 분리
//		BoardWithUserDto Detail = boardService.findBoardByNum(num);
//		Map<String, Object> map = null;
//		try {
//			map = JsonFileWriter.readJsonFileToMap("C:/board/" + Detail.getContent());
//		} catch (Exception e) {
//
//		}
//
//		if (map != null) {
//			Detail.setContent(map.get("text").toString());
//			String s = map.get("imagePath").toString();
//			System.out.println(s);
//			model.addAttribute("image", s);
//		}
		String fileName = upload.fileUpload(board.getContent());
		board.setContent(fileName);
		
		board.setNum(num);
		boardService.updateBoard(board);
		return "redirect:/AdoptDetail/" + num;
	}

	// 삭제
	@PostMapping("/AdoptDelete/{num}")
	public String adoptDelete(@PathVariable("num") int num, @AuthenticationPrincipal CustomUserDetails authUser,
			RedirectAttributes redirectAttributes) {

		// 작성자 정보 가져오기(작성자, 관라자 삭제 권한 확인용)
		BoardWithUserDto board = boardService.findBoardByNum(num);
			
		// 유저 정보 가져오기
		Users user = (Users) authUser.getUser();

		// 작성자, 관리자 삭제권한 부여
		boolean isAuthur = user.getNum() == board.getUserNum();
		boolean isAdmin = "관리자".equals(user.getGrade());

		if (!isAuthur && !isAdmin) {
			redirectAttributes.addFlashAttribute("errorMessage", "작성자만 삭제가능!");
			return "redirect:/Adopt";
		}

		
		boardService.deleteBoard(num);
		redirectAttributes.addFlashAttribute("deleteSuccess","삭제 완료되었습니다");

		return "redirect:/Adopt";
	}

	// 신고기능
	@PostMapping("/AdoptDetail/{num}/report")
	public String reportBoard(
			@PathVariable("num") int num, 
			@AuthenticationPrincipal CustomUserDetails authUser,
			RedirectAttributes redirectAttributes) {

		Users user = authUser.getUser();
		if (user != null) { // 로그인유저가 신고시 내역없으면 신고가능
			if (boardService.findReportByBC(num, user.getNum()) == null) {
				boardService.reportBoard(num, user.getNum());
			}
		}
		// 중복신고 안됌
		redirectAttributes.addFlashAttribute("reportSuccess", "신고가 정상적으로 처리되었습니다.");
		return "redirect:/AdoptDetail/" + num;
	}

	// 댓글 신고
	@PostMapping("/AdoptDetail/{num}/report/comment")
	public String reportComment(@PathVariable("num") int num, @AuthenticationPrincipal CustomUserDetails authUser) {

		Users user = authUser.getUser();
		if (user != null) {
			if (commentsService.findReportByCB(num, user.getNum()) == null) {
				commentsService.reportComment(num, user.getNum());
			}
		}

		return "redirect:/AdoptDetail/" + num;
	}

	// 게시글 좋아요
	@PostMapping("/AdoptDetail/{num}/like")
	public String likeBoard(@PathVariable("num") int num, @AuthenticationPrincipal CustomUserDetails authUser) {

		if (authUser.getUser() != null)
			likeService.likeBoard(new LikeDto(authUser.getUser().getNum(), num));

		return "redirect:/AdoptDetail/" + num;
	}

	// 댓글 등록
	@PostMapping(value = "AdoptDetail/{num}/comment")
	public String addComment(@PathVariable("num") int boardNum, // 게시글 넘버
			@RequestParam("content") String content, // 댓글내용
			@RequestParam(value = "parentNum", defaultValue = "0") int parentNum, // 대댓글 기능 없어도 됌
			@AuthenticationPrincipal CustomUserDetails authUser, // 로그인 검증용
			Model model) {

		// 로그인 유저 정보 가져오기용
		Users user = (Users) authUser.getUser();

		// 댓글 불러오기
		List<CommentInfoDto> comments = commentsService.findCommentsByBoardNum(boardNum);

		model.addAttribute("comments", comments);

		// 댓글 작성
		CommentInfoDto commentInfoDto = new CommentInfoDto();
		commentInfoDto.setUserNum(user.getNum());
		commentInfoDto.setBoardNum(boardNum);
		commentInfoDto.setContent(content);
		commentInfoDto.setParentNum(parentNum); // 대댓글기능이라 없어도 됌
		commentsService.insertComment(commentInfoDto);

		return "redirect:/AdoptDetail/" + boardNum;
	}

	// 댓글 수정
	@PostMapping("/AdoptDetail/{num}/comment/update")
	public String editComment(@PathVariable("num") int boardNum, @RequestParam("commentNum") int commentNum,
			@RequestParam("content") String content, @AuthenticationPrincipal CustomUserDetails authUser,
			RedirectAttributes redirectAttributes) {

		// 로그인 확인
		Users user = (Users) authUser.getUser();
		if (user == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
			return "redirect:/AdoptDetail/" + boardNum;
		}

		// 본인확인
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

		// html 에러메세지 보내기
		redirectAttributes.addFlashAttribute("successMessage", "수정 완료!");
		return "redirect:/AdoptDetail/" + boardNum;
	}

	// 댓글 삭제
	@PostMapping("/AdoptDetail/{num}/comment/delete")
	public String deleteComment(@AuthenticationPrincipal CustomUserDetails authUser, @PathVariable("num") int boardNum,
			@RequestParam("commentNum") int commentNum, RedirectAttributes redirectAttributes) {

		// 현재 로그인한 유저 정보
		Users loginUser = authUser.getUser();

		// 댓글 정보 조회 (작성자 확인용)
		CommentInfoDto comment = commentsService.findCommentByNum(commentNum);

		// 작성자 본인인지 확인
		if (comment.getUserNum() != loginUser.getNum()) {
			// 본인이 아니라면 삭제하지 않고 되돌림 (또는 에러 페이지로)
			redirectAttributes.addFlashAttribute("errorMessage", "작성자만 삭제 할 수 있다는 거임!");
			return "redirect:/AdoptDetail/" + boardNum;
		}

		commentsService.deleteComment(commentNum);
		redirectAttributes.addFlashAttribute("successMessage", "삭제 완료!");
		return "redirect:/AdoptDetail/" + boardNum;
	}

	// 댓글 좋아요
	@PostMapping("/AdoptDetail/{num}/comment/like")
	public String likeComment(@AuthenticationPrincipal CustomUserDetails authUser, @PathVariable("num") int boardNum,
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
		System.out.println("댓글 좋아");
		if (authUser.getUser() != null)
			commentLikeService.likeComment(new CommentLikeDto(loginUser.getNum(), commentNum));

		return "redirect:/AdoptDetail/" + boardNum;
	}
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//	// 산책로 메인페이지
//	@GetMapping(value = "/WalkTrail")
//	public String walkTrailMain(Model model, @RequestParam(value = "keyword", required = false) String keyword,
//			@RequestParam(value = "field", required = false) String field) {
//
//		// 산책로 게시판 총 게시글 수
//		int countBoards = boardService.countBoards(2);
//		model.addAttribute("countBoards", countBoards);
//
//		// 산책로 게시판 내부 검색
//		List<BoardWithUserDto> searchResult;
//		if (keyword == null || keyword.isEmpty()) {
//			// 검색 값 안넣었을 경우 전체 게시판 리스트 출력
//			searchResult = boardService.findBoardsByType(2);
//		} else {
//			switch (field) {
//			case "title":
//				searchResult = boardService.findBoardsByTitle(keyword.trim());
//				break;
//			case "content":
//				searchResult = boardService.findBoardsByContent(keyword.trim());
//				break;
//			case "nickname":
//				searchResult = boardService.findBoardsByUserId(keyword.trim());
//				break;
//			default:
//				searchResult = boardService.searchAll(keyword.trim());
//			}
//		}
//		for (var d : searchResult) {
//			System.out.println(d.getBoardNum());
//		}
//		// 메인 리스트 출력
//		model.addAttribute("mainList", searchResult);
//		// 검색값 유지 (검색창 value 유지용)
//		model.addAttribute("keyword", keyword);
//		model.addAttribute("field", field);
//
//		return "Board/WalkTrail";
//	}
//
//	// 산책로 상세 페이지 (조회수증가)
//	@GetMapping(value = "/WalkTrailDetail/{num}")
//	public String walkTrailDetail(@PathVariable("num") int num,
//			@RequestParam(value = "editCommentNum", required = false) Integer editCommentNum,
//			@AuthenticationPrincipal CustomUserDetails authUser, HttpSession session, Model model) {
//
//		// 상세 내용 보여줌
//		BoardWithUserDto Detail = boardService.findBoardByNum(num);
//		model.addAttribute("detail", Detail);
//		System.out.println("detail null? → " + (Detail == null));
//
//		// 세션에서 조회한 게시글 번호 리스트 받아오기, 없으면 만듦(조회수증가기능)
//		@SuppressWarnings("unchecked")
//		List<Integer> viewedBoards = (List<Integer>) session.getAttribute("VIEWED_BOARDS");
//		if (viewedBoards == null) {
//			viewedBoards = new ArrayList<>();
//		}
//
//		// 유저에 한해 조회수 증가
//		if (authUser != null) { // 원래 authUser는 비어있기 때문에 아래의 코드가 있으면 무조건 로그인 시킴.
//			Users user = (Users) authUser.getUser();
//			if (user != null) {
//				// 이 게시글 번호가 세션에 없으면 조회수 증가 및 세션에 저장
//				if (!viewedBoards.contains(num)) {
//					boardService.increaseViewsBoard(num);
//					viewedBoards.add(num);
//					session.setAttribute("VIEWED_BOARDS", viewedBoards);
//				}
//			}
//		}
//		// 해당 게시글 기존 댓글 보여주기
//		List<CommentInfoDto> comments = commentsService.findCommentsByBoardNum(num);
//		model.addAttribute("comments", comments);
//
//		// 댓글 수정 기능
//		if (editCommentNum != null) {
//			CommentInfoDto commentToEdit = commentsService.findCommentByNum(editCommentNum);
//			model.addAttribute("commentToEdit", commentToEdit);
//		}
//
//		System.out.println("adoptDetail 진입");
//		return "Board/WalkTrailDetail";
//	}
//
//	// 산책로 게시글 등록
//	@GetMapping(value = "/WalkTrailRegist")
//	public String walkTrailRegistForm(@AuthenticationPrincipal CustomUserDetails authUser, Model model) {
//		// 로그인 검증
//		Users user = (Users) authUser.getUser();
//		// 세션의 닉네임 저장
//		model.addAttribute("writerNick", user.getNickName());
//
//		// 글 등록시 유저번호 저장
//		BoardDto boardDto = new BoardDto();
//		boardDto.setUserNum(user.getNum());
//		boardDto.setType(2);
//		model.addAttribute("boardDto", boardDto);
//		return "Board/WalkTrailRegist";
//	}
//
//	// 산책로 게시글 등록
//	@PostMapping(value = "/WalkTrailRegist")
//	public String walkTrailRegistSubmit(
//			// 오류 검증
//			@Valid BoardDto boardDto, @RequestParam("images") List<MultipartFile> images, BindingResult bindingResult,
//			Model model) {
//
//		if (bindingResult.hasErrors()) {
//			return "Board/WalkTrailRegist";
//		}
//
//		Map<String, Object> apple = new HashMap<String, Object>();
//		apple.put("text", boardDto.getContent());
//		String str = JsonFileWriter.saveJsonToFile(apple, "C:/images/", "test");
//		boardDto.setContent(str);
//		boardService.insertBoard(boardDto);
//		return "redirect:/WalkTrail";
//	}
//
//	// 산책로 게시글 수정
//	@GetMapping(value = "/WalkTrailUpdate/{num}")
//	public String walkTrailUpdateForm(@PathVariable("num") int num, Model model,
//			@AuthenticationPrincipal CustomUserDetails authUser, RedirectAttributes redirectAttributes) {
//		// 작성자 정보 가져오기(작성자, 관라자 삭제 권한 확인용)
//		BoardWithUserDto board = boardService.findBoardByNum(num);
//
//		// 유저 정보 가져오기
//		Users user = (Users) authUser.getUser();
//
//		// 작성자, 관리자 삭제권한 부여
//		boolean isAuthur = user.getNum() == board.getUserNum();
//		boolean isAdmin = "관리자".equals(user.getGrade());
//
//		if (!isAuthur && !isAdmin) {
//			redirectAttributes.addFlashAttribute("errorMessage", "작성자만 수정가능!");
//			return "redirect:/WalkTrailDetail/" + num;
//		}
//		// 해당 게시글 내용
//		// 상세보기와 동일 코드
//		model.addAttribute("board", board); // 수정 폼에서 기본값으로 사용
//
//		return "Board/WalkTrailUpdate";
//	}
//
//	// 산책로 게시글 수정
//	@PostMapping(value = "/WalkTrailUpdate/{num}")
//	public String walkTrailUpdateSubmit(@PathVariable("num") int num, Model model, @Valid Board board,
//			BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetails authUser,
//			RedirectAttributes redirectAttributes) {
//		redirectAttributes.addAttribute("num", num);
//
//		// 유저 검증
//		Users user = (Users) authUser.getUser();
//		BoardWithUserDto existing = boardService.findBoardByNum(num);
//
//		boolean isAuthor = user.getNum() == existing.getUserNum();
//		boolean isAdmin = "관리자".equals(user.getGrade());
//
//		if (!isAuthor && !isAdmin) {
//			redirectAttributes.addFlashAttribute("errorMessage", "권한이 없습니다.");
//			return "redirect:/WalkTrailDetail" + num;
//		}
//
//		// 오류 발생시 다시 수정페이지로
//		if (bindingResult.hasErrors()) {
//			model.addAttribute("board", board); // 반드시 넣어줘야 함
//			System.out.println("45634");
//			return "/Board/WalkTrailUpdate";
//		}
//		System.out.println("123");
//		board.setNum(num);
//
//		boardService.updateBoard(board);
//		return "redirect:/AdoptDetail/" + num;
//	}
//
//	// 삭제
//	@PostMapping("/WalkTrailDelete/{num}")
//	public String walkTrailDelete(@PathVariable("num") int num, @AuthenticationPrincipal CustomUserDetails authUser,
//			RedirectAttributes redirectAttributes) {
//
//		// 작성자 정보 가져오기(작성자, 관라자 삭제 권한 확인용)
//		BoardWithUserDto board = boardService.findBoardByNum(num);
//
//		// 유저 정보 가져오기
//		Users user = (Users) authUser.getUser();
//
//		// 작성자, 관리자 삭제권한 부여
//		boolean isAuthur = user.getNum() == board.getUserNum();
//		boolean isAdmin = "관리자".equals(user.getGrade());
//
//		if (!isAuthur && !isAdmin) {
//			redirectAttributes.addFlashAttribute("errorMessage", "작성자만 삭제가능!");
//			return "redirect:/WalkTrail";
//		}
//		System.out.println("삭제진행");
//		boardService.deleteBoard(num);
//
//		return "redirect:/WalkTrail";
//	}
//
//	// 신고기능
//	@PostMapping("/WalkTrailDetail/{num}/report")
//	public String walkTrailReportBoard(@PathVariable("num") int num,
//			@AuthenticationPrincipal CustomUserDetails authUser) {
//
//		Users user = authUser.getUser();
//		if (user != null) { // 로그인유저가 신고시 내역없으면 신고가능
//			if (boardService.findReportByBC(num, user.getNum()) == null) {
//				boardService.reportBoard(num, user.getNum());
//			}
//		}
//		// 중복신고 안됌
//		return "redirect:/WalkTrailDetail/" + num;
//	}
//
//	// 댓글 신고
//	@PostMapping("/WalkTrailDetail/{num}/report/comment")
//	public String walkTrailReportComment(@PathVariable("num") int num,
//			@AuthenticationPrincipal CustomUserDetails authUser) {
//
//		Users user = authUser.getUser();
//		if (user != null) {
//			if (commentsService.findReportByCB(num, user.getNum()) == null) {
//				commentsService.reportComment(num, user.getNum());
//			}
//		}
//
//		return "redirect:/WalkTrailDetail/" + num;
//	}
//
//	// 게시글 좋아요
//	@PostMapping("/WalkTrailDetail/{num}/like")
//	public String walkTrailLikeBoard(@PathVariable("num") int num,
//			@AuthenticationPrincipal CustomUserDetails authUser) {
//
//		if (authUser.getUser() != null)
//			likeService.likeBoard(new LikeDto(authUser.getUser().getNum(), num));
//
//		return "redirect:/WalkTrailDetail/" + num;
//	}
//
//	// 댓글 등록
//	@PostMapping(value = "WalkTrailDetail/{num}/comment")
//	public String walkTrailAddComment(@PathVariable("num") int boardNum, // 게시글 넘버
//			@RequestParam("content") String content, // 댓글내용
//			@RequestParam(value = "parentNum", defaultValue = "0") int parentNum, // 대댓글 기능 없어도 됌
//			@AuthenticationPrincipal CustomUserDetails authUser, // 로그인 검증용
//			Model model) {
//
//		// 로그인 유저 정보 가져오기용
//		Users user = (Users) authUser.getUser();
//
//		// 댓글 불러오기
//		List<CommentInfoDto> comments = commentsService.findCommentsByBoardNum(boardNum);
//
//		model.addAttribute("comments", comments);
//
//		// 댓글 작성
//		CommentInfoDto commentInfoDto = new CommentInfoDto();
//		commentInfoDto.setUserNum(user.getNum());
//		commentInfoDto.setBoardNum(boardNum);
//		commentInfoDto.setContent(content);
//		commentInfoDto.setParentNum(parentNum); // 대댓글기능이라 없어도 됌
//		commentsService.insertComment(commentInfoDto);
//
//		return "redirect:/WalkTrailDetail/" + boardNum;
//	}
//
//	// 댓글 수정
//	@PostMapping("/WalkTrailDetail/{num}/comment/update")
//	public String walkTrailEditComment(@PathVariable("num") int boardNum, @RequestParam("commentNum") int commentNum,
//			@RequestParam("content") String content, @AuthenticationPrincipal CustomUserDetails authUser,
//			RedirectAttributes redirectAttributes) {
//
//		// 로그인 확인
//		Users user = (Users) authUser.getUser();
//		if (user == null) {
//			redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
//			return "redirect:/WalkTrailDetail/" + boardNum;
//		}
//
//		// 본인확인
//		CommentInfoDto originalComment = commentsService.findCommentByNum(commentNum);
//		if (originalComment == null || originalComment.getUserNum() != user.getNum()) {
//			redirectAttributes.addFlashAttribute("errorMessage", "본인 댓글만 수정할 수 있습니다.");
//			return "redirect:/WalkTrailDetail/" + boardNum;
//		}
//
//		// 댓글 내용불러오기
//		CommentInfoDto commentInfoDto = new CommentInfoDto();
//		commentInfoDto.setNum(commentNum);
//		commentInfoDto.setContent(content);
//		commentInfoDto.setUserNum(user.getNum());
//
//		commentsService.updateComment(commentInfoDto);
//
//		// html 에러메세지 보내기
//		redirectAttributes.addFlashAttribute("successMessage", "수정 완료!");
//		return "redirect:/WalkTrailDetail/" + boardNum;
//	}
//
//	// 댓글 삭제
//	@PostMapping("/WalkTrailDetail/{num}/comment/delete")
//	public String walkTrailDeleteComment(@AuthenticationPrincipal CustomUserDetails authUser,
//			@PathVariable("num") int boardNum, @RequestParam("commentNum") int commentNum,
//			RedirectAttributes redirectAttributes) {
//
//		// 현재 로그인한 유저 정보
//		Users loginUser = authUser.getUser();
//
//		// 댓글 정보 조회 (작성자 확인용)
//		CommentInfoDto comment = commentsService.findCommentByNum(commentNum);
//
//		// 작성자 본인인지 확인
//		if (comment.getUserNum() != loginUser.getNum()) {
//			// 본인이 아니라면 삭제하지 않고 되돌림 (또는 에러 페이지로)
//			redirectAttributes.addFlashAttribute("errorMessage", "작성자만 삭제 할 수 있다는 거임!");
//			return "redirect:/WalkTrailDetail/" + boardNum;
//		}
//
//		commentsService.deleteComment(commentNum);
//		redirectAttributes.addFlashAttribute("successMessage", "삭제 완료!");
//		return "redirect:/WalkTrailDetail/" + boardNum;
//	}
//
//	// 댓글 좋아요
//	@PostMapping("/WalkTrailDetail/{num}/comment/like")
//	public String walkTrailLikeComment(@AuthenticationPrincipal CustomUserDetails authUser,
//			@PathVariable("num") int boardNum, @RequestParam("commentNum") int commentNum) {
//
//		// 로그인 유저 확인
//		Users loginUser = authUser.getUser();
//		if (loginUser == null) {
//			return "redirect:/WalkTrailDetail/" + boardNum;
//		}
//
//		// 댓글 존재 확인
//		CommentInfoDto comment = commentsService.findCommentByNum(commentNum);
//		if (comment == null) {
//			return "redirect:/WalkTrailDetail/" + boardNum;
//		}
//		System.out.println("댓글 좋아");
//		if (authUser.getUser() != null)
//			commentLikeService.likeComment(new CommentLikeDto(loginUser.getNum(), commentNum));
//
//		return "redirect:/WalkTrailDetail/" + boardNum;
//	}
//	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//	// 중고장터 메인페이지
//	@GetMapping(value = "/UsedGoods")
//	public String usedGoodsMain(Model model, @RequestParam(value = "keyword", required = false) String keyword,
//			@RequestParam(value = "field", required = false) String field) {
//
//		// 중고장터 게시판 총 게시글 수
//		int countBoards = boardService.countBoards(3);
//		model.addAttribute("countBoards", countBoards);
//
//		// 중고장터 게시판 내부 검색
//		List<BoardWithUserDto> searchResult;
//		if (keyword == null || keyword.isEmpty()) {
//			// 검색 값 안넣었을 경우 전체 게시판 리스트 출력
//			searchResult = boardService.findBoardsByType(3);
//		} else {
//			switch (field) {
//			case "title":
//				searchResult = boardService.findBoardsByTitle(keyword.trim());
//				break;
//			case "content":
//				searchResult = boardService.findBoardsByContent(keyword.trim());
//				break;
//			case "nickname":
//				searchResult = boardService.findBoardsByUserId(keyword.trim());
//				break;
//			default:
//				searchResult = boardService.searchAll(keyword.trim());
//			}
//		}
//		for (var d : searchResult) {
//			System.out.println(d.getBoardNum());
//		}
//		// 메인 리스트 출력
//		model.addAttribute("mainList", searchResult);
//		// 검색값 유지 (검색창 value 유지용)
//		model.addAttribute("keyword", keyword);
//		model.addAttribute("field", field);
//
//		return "Board/UsedGoods";
//	}
//
//	// 중고장터 상세 페이지 (조회수증가)
//	@GetMapping(value = "/UsedGoodsDetail/{num}")
//	public String usedGoodsDetail(@PathVariable("num") int num,
//			@RequestParam(value = "editCommentNum", required = false) Integer editCommentNum,
//			@AuthenticationPrincipal CustomUserDetails authUser, HttpSession session, Model model) {
//
//		// 상세 내용 보여줌
//		BoardWithUserDto Detail = boardService.findBoardByNum(num);
//		model.addAttribute("detail", Detail);
//		System.out.println("detail null? → " + (Detail == null));
//
//		// 세션에서 조회한 게시글 번호 리스트 받아오기, 없으면 만듦(조회수증가기능)
//		@SuppressWarnings("unchecked")
//		List<Integer> viewedBoards = (List<Integer>) session.getAttribute("VIEWED_BOARDS");
//		if (viewedBoards == null) {
//			viewedBoards = new ArrayList<>();
//		}
//
//		// 유저에 한해 조회수 증가
//		if (authUser != null) { // 원래 authUser는 비어있기 때문에 아래의 코드가 있으면 무조건 로그인 시킴.
//			Users user = (Users) authUser.getUser();
//			if (user != null) {
//				// 이 게시글 번호가 세션에 없으면 조회수 증가 및 세션에 저장
//				if (!viewedBoards.contains(num)) {
//					boardService.increaseViewsBoard(num);
//					viewedBoards.add(num);
//					session.setAttribute("VIEWED_BOARDS", viewedBoards);
//				}
//			}
//		}
//		// 해당 게시글 기존 댓글 보여주기
//		List<CommentInfoDto> comments = commentsService.findCommentsByBoardNum(num);
//		model.addAttribute("comments", comments);
//
//		// 댓글 수정 기능
//		if (editCommentNum != null) {
//			CommentInfoDto commentToEdit = commentsService.findCommentByNum(editCommentNum);
//			model.addAttribute("commentToEdit", commentToEdit);
//		}
//
//		System.out.println("Detail 진입");
//		return "Board/UsedGoodsDetail";
//	}
//
//	// 중고장터 게시글 등록
//	@GetMapping(value = "/UsedGoodsRegist")
//	public String usedGoodsRegistForm(@AuthenticationPrincipal CustomUserDetails authUser, Model model) {
//		// 로그인 검증
//		Users user = (Users) authUser.getUser();
//		// 세션의 닉네임 저장
//		model.addAttribute("writerNick", user.getNickName());
//
//		// 글 등록시 유저번호 저장
//		BoardDto boardDto = new BoardDto();
//		boardDto.setUserNum(user.getNum());
//		boardDto.setType(3);
//		model.addAttribute("boardDto", boardDto);
//		return "Board/UsedGoodsRegist";
//	}
//
//	// 중고장터 게시글 등록
//	@PostMapping(value = "/UsedGoodsRegist")
//	public String usedGoodsRegistSubmit(
//			// 오류 검증
//			@Valid BoardDto boardDto, @RequestParam("images") List<MultipartFile> images, BindingResult bindingResult,
//			Model model) {
//
//		if (bindingResult.hasErrors()) {
//			return "Board/UsedGoodsRegist";
//		}
//
//		Map<String, Object> apple = new HashMap<String, Object>();
//		apple.put("text", boardDto.getContent());
//		String str = JsonFileWriter.saveJsonToFile(apple, "C:/images/", "test");
//		boardDto.setContent(str);
//		boardService.insertBoard(boardDto);
//		return "redirect:/UsedGoods";
//	}
//
//	// 중고장터 게시글 수정
//	@GetMapping(value = "/UsedGoodsUpdate/{num}")
//	public String usedGoodsUpdateForm(@PathVariable("num") int num, Model model,
//			@AuthenticationPrincipal CustomUserDetails authUser, RedirectAttributes redirectAttributes) {
//		// 작성자 정보 가져오기(작성자, 관라자 삭제 권한 확인용)
//		BoardWithUserDto board = boardService.findBoardByNum(num);
//
//		// 유저 정보 가져오기
//		Users user = (Users) authUser.getUser();
//
//		// 작성자, 관리자 삭제권한 부여
//		boolean isAuthur = user.getNum() == board.getUserNum();
//		boolean isAdmin = "관리자".equals(user.getGrade());
//
//		if (!isAuthur && !isAdmin) {
//			redirectAttributes.addFlashAttribute("errorMessage", "작성자만 수정가능!");
//			return "redirect:/UsedGoodsDetail/" + num;
//		}
//		// 해당 게시글 내용
//		// 상세보기와 동일 코드
//		model.addAttribute("board", board); // 수정 폼에서 기본값으로 사용
//
//		return "Board/UsedGoodsUpdate";
//	}
//
//	// 중고장터 게시글 수정
//	@PostMapping(value = "/UsedGoodsUpdate/{num}")
//	public String usedGoodsUpdateSubmit(@PathVariable("num") int num, Model model, @Valid Board board,
//			BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetails authUser,
//			RedirectAttributes redirectAttributes) {
//		redirectAttributes.addAttribute("num", num);
//
//		// 유저 검증
//		Users user = (Users) authUser.getUser();
//		BoardWithUserDto existing = boardService.findBoardByNum(num);
//
//		boolean isAuthor = user.getNum() == existing.getUserNum();
//		boolean isAdmin = "관리자".equals(user.getGrade());
//
//		if (!isAuthor && !isAdmin) {
//			redirectAttributes.addFlashAttribute("errorMessage", "권한이 없습니다.");
//			return "redirect:/UsedGoodsDetail" + num;
//		}
//
//		// 오류 발생시 다시 수정페이지로
//		if (bindingResult.hasErrors()) {
//			model.addAttribute("board", board); // 반드시 넣어줘야 함
//			System.out.println("45634");
//			return "/Board/UsedGoodsUpdate";
//		}
//		System.out.println("123");
//		board.setNum(num);
//
//		boardService.updateBoard(board);
//		return "redirect:/UsedGoodsDetail/" + num;
//	}
//
//	// 삭제
//	@PostMapping("/UsedGoodsDelete/{num}")
//	public String usedGoodsDelete(@PathVariable("num") int num, @AuthenticationPrincipal CustomUserDetails authUser,
//			RedirectAttributes redirectAttributes) {
//
//		// 작성자 정보 가져오기(작성자, 관라자 삭제 권한 확인용)
//		BoardWithUserDto board = boardService.findBoardByNum(num);
//
//		// 유저 정보 가져오기
//		Users user = (Users) authUser.getUser();
//
//		// 작성자, 관리자 삭제권한 부여
//		boolean isAuthur = user.getNum() == board.getUserNum();
//		boolean isAdmin = "관리자".equals(user.getGrade());
//
//		if (!isAuthur && !isAdmin) {
//			redirectAttributes.addFlashAttribute("errorMessage", "작성자만 삭제가능!");
//			return "redirect:/WalkTrail";
//		}
//		System.out.println("삭제진행");
//		boardService.deleteBoard(num);
//
//		return "redirect:/UsedGoods";
//	}
//
//	// 신고기능
//	@PostMapping("/UsedGoodsDetail/{num}/report")
//	public String usedGoodsReportBoard(@PathVariable("num") int num,
//			@AuthenticationPrincipal CustomUserDetails authUser) {
//
//		Users user = authUser.getUser();
//		if (user != null) { // 로그인유저가 신고시 내역없으면 신고가능
//			if (boardService.findReportByBC(num, user.getNum()) == null) {
//				boardService.reportBoard(num, user.getNum());
//			}
//		}
//		// 중복신고 안됌
//		return "redirect:/UsedGoodsDetail/" + num;
//	}
//
//	// 댓글 신고
//	@PostMapping("/UsedGoodsDetail/{num}/report/comment")
//	public String usedGoodsReportComment(@PathVariable("num") int num,
//			@AuthenticationPrincipal CustomUserDetails authUser) {
//
//		Users user = authUser.getUser();
//		if (user != null) {
//			if (commentsService.findReportByCB(num, user.getNum()) == null) {
//				commentsService.reportComment(num, user.getNum());
//			}
//		}
//
//		return "redirect:/UsedGoodsDetail/" + num;
//	}
//
//	// 게시글 좋아요
//	@PostMapping("/UsedGoodsDetail/{num}/like")
//	public String usedGoodsLikeBoard(@PathVariable("num") int num,
//			@AuthenticationPrincipal CustomUserDetails authUser) {
//
//		if (authUser.getUser() != null)
//			likeService.likeBoard(new LikeDto(authUser.getUser().getNum(), num));
//
//		return "redirect:/UsedGoodsDetail/" + num;
//	}
//
//	// 댓글 등록
//	@PostMapping(value = "UsedGoodsDetail/{num}/comment")
//	public String usedGoodsAddComment(@PathVariable("num") int boardNum, // 게시글 넘버
//			@RequestParam("content") String content, // 댓글내용
//			@RequestParam(value = "parentNum", defaultValue = "0") int parentNum, // 대댓글 기능 없어도 됌
//			@AuthenticationPrincipal CustomUserDetails authUser, // 로그인 검증용
//			Model model) {
//
//		// 로그인 유저 정보 가져오기용
//		Users user = (Users) authUser.getUser();
//
//		// 댓글 불러오기
//		List<CommentInfoDto> comments = commentsService.findCommentsByBoardNum(boardNum);
//
//		model.addAttribute("comments", comments);
//
//		// 댓글 작성
//		CommentInfoDto commentInfoDto = new CommentInfoDto();
//		commentInfoDto.setUserNum(user.getNum());
//		commentInfoDto.setBoardNum(boardNum);
//		commentInfoDto.setContent(content);
//		commentInfoDto.setParentNum(parentNum); // 대댓글기능이라 없어도 됌
//		commentsService.insertComment(commentInfoDto);
//
//		return "redirect:/UsedGoodsDetail/" + boardNum;
//	}
//
//	// 댓글 수정
//	@PostMapping("/UsedGoodsDetail/{num}/comment/update")
//	public String usedGoodsEditComment(@PathVariable("num") int boardNum, @RequestParam("commentNum") int commentNum,
//			@RequestParam("content") String content, @AuthenticationPrincipal CustomUserDetails authUser,
//			RedirectAttributes redirectAttributes) {
//
//		// 로그인 확인
//		Users user = (Users) authUser.getUser();
//		if (user == null) {
//			redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
//			return "redirect:/UsedGoodsDetail/" + boardNum;
//		}
//
//		// 본인확인
//		CommentInfoDto originalComment = commentsService.findCommentByNum(commentNum);
//		if (originalComment == null || originalComment.getUserNum() != user.getNum()) {
//			redirectAttributes.addFlashAttribute("errorMessage", "본인 댓글만 수정할 수 있습니다.");
//			return "redirect:/UsedGoodsDetail/" + boardNum;
//		}
//
//		// 댓글 내용불러오기
//		CommentInfoDto commentInfoDto = new CommentInfoDto();
//		commentInfoDto.setNum(commentNum);
//		commentInfoDto.setContent(content);
//		commentInfoDto.setUserNum(user.getNum());
//
//		commentsService.updateComment(commentInfoDto);
//
//		// html 에러메세지 보내기
//		redirectAttributes.addFlashAttribute("successMessage", "수정 완료!");
//		return "redirect:/UsedGoodsDetail/" + boardNum;
//	}
//
//	// 댓글 삭제
//	@PostMapping("/UsedGoodsDetail/{num}/comment/delete")
//	public String usedGoodsDeleteComment(@AuthenticationPrincipal CustomUserDetails authUser,
//			@PathVariable("num") int boardNum, @RequestParam("commentNum") int commentNum,
//			RedirectAttributes redirectAttributes) {
//
//		// 현재 로그인한 유저 정보
//		Users loginUser = authUser.getUser();
//
//		// 댓글 정보 조회 (작성자 확인용)
//		CommentInfoDto comment = commentsService.findCommentByNum(commentNum);
//
//		// 작성자 본인인지 확인
//		if (comment.getUserNum() != loginUser.getNum()) {
//			// 본인이 아니라면 삭제하지 않고 되돌림 (또는 에러 페이지로)
//			redirectAttributes.addFlashAttribute("errorMessage", "작성자만 삭제 할 수 있다는 거임!");
//			return "redirect:/UsedGoodsDetail/" + boardNum;
//		}
//
//		commentsService.deleteComment(commentNum);
//		redirectAttributes.addFlashAttribute("successMessage", "삭제 완료!");
//		return "redirect:/UsedGoodsDetail/" + boardNum;
//	}
//
//	// 댓글 좋아요
//	@PostMapping("/UsedGoodsDetail/{num}/comment/like")
//	public String usedGoodsLikeComment(@AuthenticationPrincipal CustomUserDetails authUser,
//			@PathVariable("num") int boardNum, @RequestParam("commentNum") int commentNum) {
//
//		// 로그인 유저 확인
//		Users loginUser = authUser.getUser();
//		if (loginUser == null) {
//			return "redirect:/UsedGoodsDetail/" + boardNum;
//		}
//
//		// 댓글 존재 확인
//		CommentInfoDto comment = commentsService.findCommentByNum(commentNum);
//		if (comment == null) {
//			return "redirect:/UsedGoodsDetail/" + boardNum;
//		}
//		System.out.println("댓글 좋아");
//		if (authUser.getUser() != null)
//			commentLikeService.likeComment(new CommentLikeDto(loginUser.getNum(), commentNum));
//
//		return "redirect:/UsedGoodsDetail/" + boardNum;
//	}
//
//}
