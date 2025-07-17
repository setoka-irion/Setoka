package com.practice.setoka.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.setoka.dao.Animal;
import com.practice.setoka.dto.AnimalDto;
import com.practice.setoka.mapper.AnimalMapper;

@Service
public class AnimalService {

    @Autowired
    private AnimalMapper animalMapper;
    
    // 유저 번호로 해당 유저가 가진 모든 애견 목록 조회
    public List<Animal> getAnimalsByUserNum(int userNum) {
        return animalMapper.selectByUserNum(userNum);
    }

    // DTO를 받아 새 애견 등록
    public boolean insertAnimal(AnimalDto animalDto) {
    	if(animalDto.getProfilePath() == null)
    		return animalMapper.insertAnimal2(animalDto);
    	
        return animalMapper.insertAnimal(animalDto);
    }

    // DTO를 받아 애견 정보 수정
    public boolean updateAnimal(int num, AnimalDto animalDto) {
        return animalMapper.updateAnimal(animalDto, num);
    }

    // 애견 삭제
    public boolean deleteAnimal(int num) {
        return animalMapper.deleteAnimal(num);
    }

    // 단일 애견 조회
    public Animal getAnimalByNum(int num) {
        return animalMapper.selectByNum(num);
    }
    
    //num 값으로 동물이름 반환
    public String selectAnimalNameByNum(int num) {
    	return animalMapper.selectAnimalNameByNum(num);
    }
    
	// 유저의 애견 목록 가져오기
    public List<Animal> getPetsByUser(int userNum) {
        return animalMapper.selectByUserNum(userNum);
    }

}

