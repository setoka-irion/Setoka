package com.practice.setoka.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.practice.setoka.Upload;
import com.practice.setoka.dao.Animal;
import com.practice.setoka.dao.Memo;
import com.practice.setoka.dao.Users;
import com.practice.setoka.dto.AnimalDto;
import com.practice.setoka.service.AnimalService;
import com.practice.setoka.service.MemoService;
import com.practice.setoka.springSecurity.CustomUserDetails;

@Controller
public class MyAnimalPageController {

    @Autowired
    private AnimalService animalService;
    @Autowired
    private MemoService memoService;
    @Autowired
    private Upload upload;
    
    @GetMapping("/myanimal")
    public String showMyAnimalPage(Model model, @AuthenticationPrincipal CustomUserDetails authUser) {
        Users user = (Users) authUser.getUser();
        int userNum = user.getNum();

        List<Animal> animals = animalService.getAnimalsByUserNumNormal(userNum);
        model.addAttribute("animalList", animals);

        return "MyPage/MyAnimalPage";
    }
    
    // 애견 추가
    @PostMapping("/myanimal/add")
    public String addAnimal(
				        AnimalDto animalDto, @RequestParam("profileImage") MultipartFile file,
				        @RequestParam("togetherDateStr") String togetherDateStr,
				        @AuthenticationPrincipal CustomUserDetails authUser) {
        Users user = (Users) authUser.getUser();

        animalDto.setUserNum(user.getNum());

        if (file != null && !file.isEmpty()) {
            animalDto.setProfilePath(upload.imageFileUpload(file));
        }
        
        // 날짜 문자열 → LocalDateTime 변환
        LocalDateTime dateTime = LocalDate.parse(togetherDateStr).atStartOfDay();
        animalDto.setTogetherDate(dateTime);

        animalService.insertAnimal(animalDto);
        return "redirect:/myanimal";
    }

    // 애견 삭제
    @PostMapping("/myanimal/delete")
    public String deleteAnimal(@RequestParam("animalNum") int animalNum) {
        animalService.updateDeleteAnimal(animalNum);
        return "redirect:/myanimal";
    }
    
    // 애견 수정
    @GetMapping("/myanimal/edit")
    @ResponseBody
    public Animal getAnimalForEdit(@RequestParam("animalNum") int animalNum, @AuthenticationPrincipal CustomUserDetails authUser) {
    	Animal animal = animalService.getAnimalByNum(animalNum);
    	Users user = authUser.getUser();
    	if(animal!=null&&animal.getUserNum()==user.getNum()) {
    		return animalService.getAnimalByNum(animalNum);
    	}
    	return null;
    }
    
    @PostMapping("/myanimal/edit")
    @ResponseBody
    public String updateAnimal(
    	    @RequestParam("animalNum") int animalNum,
    	    AnimalDto animalDto,
    	    @RequestParam("togetherDateStr") String togetherDateStr,
    	    @RequestParam(value = "profileImage", required = false) MultipartFile file,
    	    @RequestParam(value = "existingProfilePath", required = false) String existingProfilePath,
    	    @AuthenticationPrincipal CustomUserDetails authUser) {

    	    Users user = (Users) authUser.getUser();

    	    animalDto.setUserNum(user.getNum());

    	    if (file != null && !file.isEmpty()) {
    	        // 새 이미지 업로드
    	        animalDto.setProfilePath(upload.imageFileUpload(file));
    	    } else {
    	        // 이미지 미변경 시 기존 경로 유지
    	        animalDto.setProfilePath(null);
    	    }

    	    LocalDateTime dateTime = LocalDate.parse(togetherDateStr).atStartOfDay();
    	    animalDto.setTogetherDate(dateTime);

    	    animalService.updateAnimal(animalNum, animalDto);
    	    return "success";
    	}
    
    @GetMapping("/animal/detail")
    public String animalDetail(Model model,
            @RequestParam(name = "animalNum") int animalNum,
            @RequestParam(name = "year", required = false) Integer year,
            @RequestParam(name = "month", required = false) Integer month,
            @AuthenticationPrincipal CustomUserDetails authUser) {

        Users user = (Users) authUser.getUser();

        int userNum = user.getNum();
        model.addAttribute("userNum", userNum);
        model.addAttribute("animalNum", animalNum);
        
        LocalDate now = LocalDate.now();
        if (year == null || month == null) {
            year = now.getYear();
            month = now.getMonthValue();
        }

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        int lengthOfMonth = yearMonth.lengthOfMonth();

        int firstDayWeekValue = firstDayOfMonth.getDayOfWeek().getValue(); // 1=월, ... 7=일
        int startBlank = firstDayWeekValue % 7;

        int totalCells = startBlank + lengthOfMonth;
        int weekCount = (int) Math.ceil(totalCells / 7.0);

        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("lengthOfMonth", lengthOfMonth);
        model.addAttribute("startBlank", startBlank);
        model.addAttribute("weekCount", weekCount);

        // animal detail 데이터는 따로 넣기
        Animal animal = animalService.getAnimalByNum(animalNum);
        model.addAttribute("CurrentAnimal", animal);

        List<Memo> memos = memoService.memoSelectByAnimalNum(animalNum);
        model.addAttribute("memos", memos);
        
        return "redirect:/MyPage?year=" + year + "&month=" + month;
    }


}
