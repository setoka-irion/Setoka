package com.practice.setoka.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.practice.setoka.Redirect;
import com.practice.setoka.dao.Animal;
import com.practice.setoka.dao.Users;
import com.practice.setoka.dto.AnimalDto;
import com.practice.setoka.service.AnimalService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MyAnimalPageController {

    @Autowired
    private AnimalService animalService;

    @GetMapping("/myanimal")
    public String showMyAnimalPage(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute(Redirect.loginSession);
        if (user == null) {
            return "redirect:/Login";
        }
        int userNum = user.getNum();

        List<Animal> animals = animalService.getAnimalsByUserNum(userNum);
        model.addAttribute("animalList", animals);

        return "MyAnimalPage";
    }

    @GetMapping("/myanimal/edit")
    @ResponseBody
    public Animal getAnimalForEdit(@RequestParam("animalNum") int animalNum) {
        return animalService.getAnimalByNum(animalNum);
    }

    // 애견 추가
    @PostMapping("/myanimal/add")
    public String addAnimal(
        AnimalDto animalDto,
        @RequestParam("togetherDateStr") String togetherDateStr,
        HttpSession session) {
    	System.out.println("gender 값 확인: " + animalDto.getGender());
        Users user = (Users) session.getAttribute(Redirect.loginSession);
        if (user == null) {
            return "redirect:/Login";
        }
        animalDto.setUserNum(user.getNum());

        // 날짜 문자열 → LocalDateTime 변환
        LocalDateTime dateTime = LocalDate.parse(togetherDateStr).atStartOfDay();
        animalDto.setTogetherDate(dateTime);

        animalService.insertAnimal(animalDto);
        return "redirect:/myanimal";
    }

    // 애견 삭제
    @PostMapping("/myanimal/delete")
    public String deleteAnimal(@RequestParam("animalNum") int animalNum) {
        animalService.deleteAnimal(animalNum);
        return "redirect:/myanimal";
    }

    // 애견 수정 처리
    @PostMapping("/myanimal/edit")
    @ResponseBody
    public String updateAnimal(
        @RequestParam("animalNum") int animalNum,
        AnimalDto animalDto,
        @RequestParam("togetherDateStr") String togetherDateStr,
        HttpSession session) {
    	
        Users user = (Users) session.getAttribute(Redirect.loginSession);
        if (user == null) {
            return "redirect:/Login";
        }
        animalDto.setUserNum(user.getNum());

        LocalDateTime dateTime = LocalDate.parse(togetherDateStr).atStartOfDay();
        animalDto.setTogetherDate(dateTime);

        animalService.updateAnimal(animalNum, animalDto);
        return "success";
    }
}
