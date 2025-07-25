package com.practice.setoka.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.practice.setoka.dto.CommentLikeDto;
import com.practice.setoka.mapper.CommentLikeMapper;

@Service
public class CommentLikeService {

		private final CommentLikeMapper commentLikeMapper;
		
		public CommentLikeService(CommentLikeMapper commentLikeMapper) {
			this.commentLikeMapper = commentLikeMapper;
		}
		
		// 댓글 좋아요 중복검사
		public int existCommentLike(CommentLikeDto commentLikeDto) {
			return commentLikeMapper.existCommentLike(commentLikeDto);
		}
		
		//댓글 좋아요 로그 기록
		public void insertCommentLike(CommentLikeDto commentLikeDto) {
			commentLikeMapper.insertCommentLike(commentLikeDto.getUserNum(), commentLikeDto.getCommentNum());
		}
		
		//댓글 좋아요 로그 기록 지우기
		public void disLikeComment(CommentLikeDto commentLikeDto) {
			commentLikeMapper.disLikeComment(commentLikeDto);
		}
		
		// 댓글 좋아요 횟수 증가
		public void increaseLikesComment(int commentNum) {
			commentLikeMapper.increaseLikesComment(commentNum);
		}
		
		// 댓글 좋아요 횟수 감소
		public void cancelLikesComment(int commentNum) {
			commentLikeMapper.cancelLikesComment(commentNum);
		}
		
		// 통합 댓글 좋아요 기능
		@Transactional
		public void likeComment(CommentLikeDto commentLikeDto) {
			if(commentLikeMapper.existCommentLike(commentLikeDto) == 0) {
				commentLikeMapper.insertCommentLike(commentLikeDto.getUserNum(), commentLikeDto.getCommentNum());
				commentLikeMapper.increaseLikesComment(commentLikeDto.getCommentNum());
			}else {
				commentLikeMapper.disLikeComment(commentLikeDto);
				commentLikeMapper.cancelLikesComment(commentLikeDto.getCommentNum());
			}
		}
}
