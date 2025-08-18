package com.practice.setoka.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.practice.setoka.dao.Memo;
import com.practice.setoka.dto.MemoDto;

@Mapper
public interface MemoMapper {
	List<Memo> memoSelectAll();
	Memo memoSelectByNum(@Param("num") int num);
    List<Memo> memoSelectByUserNum(@Param("userNum") int userNum);
    List<Memo> memoSelectByAnimalNum(@Param("animalNum") int animalNum);
    List<Memo> memoSelectByMonth(@Param("year") int year, @Param("month") int month);
    List<Memo> memoSelectByUserNumAndMonth(Map<String, Object> params);
    int insertMemo(MemoDto memoDto);
    int updateMemo(@Param("num") int num, @Param("memoDto") MemoDto memoDto);
    int deleteMemo(@Param("num") int num);
}
