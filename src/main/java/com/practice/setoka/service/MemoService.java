package com.practice.setoka.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.practice.setoka.dao.Memo;
import com.practice.setoka.dto.MemoDto;
import com.practice.setoka.mapper.MemoMapper;

@Service
public class MemoService {

	@Autowired
	private MemoMapper memoMapper;
	
	public List<Memo> memoSelectAll() {
		return memoMapper.memoSelectAll();
	}
	
	public Memo memoSelectByNum(int num) {
		return memoMapper.memoSelectByNum(num);
	}
	
	public List<Memo> memoSelectByUserNum(int userNum) {
		return memoMapper.memoSelectByUserNum(userNum);
	}
	
	public List<Memo> memoSelectByAnimalNum(int animalNum) {
		return memoMapper.memoSelectByAnimalNum(animalNum);
	}
	
	 public List<Memo> memoSelectByMonth(int year, int month) {
		return memoMapper.memoSelectByMonth(year, month);
	}
	 
	public int insertMemo(MemoDto memoDto) {
		return memoMapper.insertMemo(memoDto);
	}
	
	public int updateMemo(int num, MemoDto memoDto) {
		return memoMapper.updateMemo(num, memoDto);
	}
	
	public int deleteMemo(int num) {
		return memoMapper.deleteMemo(num);
	}
}

