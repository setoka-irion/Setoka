package com.practice.setoka.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.practice.setoka.dao.Animal;
import com.practice.setoka.service.AnimalService;

@RestController
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    // 유저번호로 해당 유저의 애견 목록 반환 (JSON)
    @GetMapping("/animals")
    public List<Animal> getAnimalsByUserNum(@RequestParam(name = "userNum") int userNum) {
        return animalService.getAnimalsByUserNum(userNum);
    }
}

