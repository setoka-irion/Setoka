package com.practice.setoka.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.practice.setoka.dao.Animal;

@Mapper
public interface AnimalMapper {

    // 유저 번호로 동물 리스트 조회
    List<Animal> selectByUserNum(@Param("userNum") int userNum);

    // 동물 한 건 조회 (고유번호 기준)
    Animal selectByNum(@Param("num") int num);

    // 동물 등록
    int insertAnimal(Animal animal);

    // 동물 수정
    int updateAnimal(Animal animal);

    // 동물 삭제
    int deleteAnimal(@Param("num") int num);
}

