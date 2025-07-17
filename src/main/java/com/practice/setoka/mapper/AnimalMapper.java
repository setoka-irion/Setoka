package com.practice.setoka.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.practice.setoka.dao.Animal;
import com.practice.setoka.dto.AnimalDto;

@Mapper
public interface AnimalMapper {

    // 유저 번호로 동물 리스트 조회
    List<Animal> selectByUserNum(@Param("userNum") int userNum);

    // 동물 한 건 조회 (고유번호 기준)
    Animal selectByNum(@Param("num") int num);
    
    //num 값으로 동물이름 반환
    String selectAnimalNameByNum(@Param("num")int num);

    // 동물 등록
    boolean insertAnimal(AnimalDto animalDto);
    
    boolean insertAnimal2(AnimalDto animalDto);

    // 동물 수정
    boolean updateAnimal(@Param("animalDto") AnimalDto animalDto, @Param("num") int num);

    // 동물 삭제
    boolean deleteAnimal(@Param("num") int num);
    
}

