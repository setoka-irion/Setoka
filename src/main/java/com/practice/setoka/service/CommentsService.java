package com.practice.setoka.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.practice.setoka.dao.Comments;
import com.practice.setoka.dao.Report;
import com.practice.setoka.dto.CommentInfoDto;
import com.practice.setoka.mapper.CommentsMapper;

@Service
@Transactional
public class CommentsService {

	private final CommentsMapper commentsMapper;

	public CommentsService(CommentsMapper commentsMapper) {
		this.commentsMapper = commentsMapper;
	}

	// 등록
	public void insertComment(CommentInfoDto commentInfoDto) {
		commentsMapper.insertComment(commentInfoDto);
	}

	// 수정
	public void updateComment(CommentInfoDto commentInfoDto) {
		commentsMapper.updateComment(commentInfoDto);
	}

	// 삭제
	public void deleteComment(int num) {
		commentsMapper.deleteComment(num);
	}

	// 좋아요
	public void increaseCommentLikes(int num) {
		commentsMapper.increaseCommentLikes(num);
	}

	// 해당 게시글의 댓글 보기
	public List<CommentInfoDto> findCommentsByBoardNum(int boardNum) {
		return commentsMapper.findCommentsByBoardNum(boardNum);
	}

	// 댓글 수정용 댓글 하나 불러오기
	public CommentInfoDto findCommentByNum(int num) {
		return commentsMapper.findCommentByNum(num);
	}

	
	// 신고
	public void reportComment(int commentNum, int userNum) {
		commentsMapper.reportComment(commentNum, userNum);
	}

	// 신고

	// 게시판 신고 유저 내용
	public Report findReportByCB(int commentNum, int userNum) {
		return commentsMapper.findReportByCB(commentNum, userNum);
	}

}
