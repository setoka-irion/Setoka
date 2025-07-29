package com.practice.setoka.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.practice.setoka.dto.LikeDto;
import com.practice.setoka.mapper.LikeMapper;

@Service
public class LikeService {
	
	private final LikeMapper likeMapper;
	
	public LikeService(LikeMapper likeMapper) {
        this.likeMapper = likeMapper;
    }
	
	
	// 좋아요 중복검사
	public int existLike(LikeDto likeDto) {
		return likeMapper.existLike(likeDto);	
	}
	 
	// 좋아요 증가기능
	public void insertLike(LikeDto likeDto) {
		likeMapper.insertLike(likeDto.getUserNum(), likeDto.getBoardNum());
	}
	
	// 좋아요 취소기능
	public void disLike(LikeDto likeDto) {
		likeMapper.disLike(likeDto);
	}
	
	// 좋아요 
	public void increaseLikesBoard(int boardNum) {
		likeMapper.increaseLikesBoard(boardNum);
	}
	
	// 좋아요 취소 
	public void cancelLikesBoard(int boardNum) {
		likeMapper.cancelLikesBoard(boardNum);
	}
	
	// 통합 좋아요 기능
	@Transactional
	public void likeBoard(LikeDto likeDto) {
		if(likeMapper.existLike(likeDto) == 0) { //중복 체크
			likeMapper.insertLike(likeDto.getUserNum(), likeDto.getBoardNum()); //로그 기록
			likeMapper.increaseLikesBoard(likeDto.getBoardNum()); //숫자증가
		}
		else // 
		{
			likeMapper.disLike(likeDto);
			likeMapper.cancelLikesBoard(likeDto.getBoardNum());
		}
		
	}
}
