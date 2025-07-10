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
    public int insertAnimal(AnimalDto animalDto) {
        Animal animal = new Animal();
        animal.setUserNum(animalDto.getUserNum());
        animal.setAnimalName(animalDto.getAnimalName());
        animal.setSpecies(animalDto.getSpecies());
        animal.setAge(animalDto.getAge());
        animal.setStatus(animalDto.getStatus());
        animal.setGender(animalDto.getGender());
        animal.setTogetherDate(animalDto.getTogetherDate());

        return animalMapper.insertAnimal(animal);
    }

    // DTO를 받아 애견 정보 수정
    public int updateAnimal(int num, AnimalDto animalDto) {
        Animal animal = new Animal();
        animal.setNum(num);
        animal.setUserNum(animalDto.getUserNum());
        animal.setAnimalName(animalDto.getAnimalName());
        animal.setSpecies(animalDto.getSpecies());
        animal.setAge(animalDto.getAge());
        animal.setStatus(animalDto.getStatus());
        animal.setGender(animalDto.getGender());
        animal.setTogetherDate(animalDto.getTogetherDate());

        return animalMapper.updateAnimal(animal);
    }

    // 애견 삭제
    public int deleteAnimal(int num) {
        return animalMapper.deleteAnimal(num);
    }

    // 단일 애견 조회
    public Animal getAnimalByNum(int num) {
        return animalMapper.selectByNum(num);
    }
}

