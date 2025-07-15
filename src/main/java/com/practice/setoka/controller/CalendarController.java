package com.practice.setoka.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.practice.setoka.Redirect;
import com.practice.setoka.dao.Animal;
import com.practice.setoka.dao.Memo;
import com.practice.setoka.dao.Users;
import com.practice.setoka.dto.MemoDto;
import com.practice.setoka.service.AnimalService;
import com.practice.setoka.service.MemoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CalendarController {

    @Autowired
    private MemoService memoService;
    @Autowired
    private AnimalService animalService;

    @GetMapping("/calendar")
    public String calendar(Model model,
            @RequestParam(name = "year", required = false) Integer year,
            @RequestParam(name = "month", required = false) Integer month,
            HttpSession session) {

    	Users user = (Users) session.getAttribute(Redirect.loginSession);
    	if (user == null) {
    	    return "redirect:/Login";
    	}
    	int userNum = user.getNum();
    	model.addAttribute("userNum", userNum);
    	
        LocalDate now = LocalDate.now();
        if (year == null || month == null) {
            year = now.getYear();
            month = now.getMonthValue();
        }

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        int lengthOfMonth = yearMonth.lengthOfMonth();

        // 요일을 일요일부터 시작하는 순서로 맞춤 (1:월요일 ~ 7:일요일 -> 7을 먼저)
        int firstDayWeekValue = firstDayOfMonth.getDayOfWeek().getValue(); // 1=월, ... 7=일
        // 일요일부터 시작하려면 아래 식으로 변환 (일요일=0, 월=1,... 토=6)
        int startBlank = firstDayWeekValue % 7;

        int totalCells = startBlank + lengthOfMonth;
        int weekCount = (int) Math.ceil(totalCells / 7.0);
        
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("lengthOfMonth", lengthOfMonth);
        model.addAttribute("startBlank", startBlank);
        model.addAttribute("weekCount", weekCount);
        
        List<Animal> animals = animalService.getAnimalsByUserNum(userNum);
        model.addAttribute("animals", animals);
        
        return "Calendar";
    }

    @PostMapping("/memo/add")
    public String addMemo(MemoDto memoDto) {
        LocalDateTime dt = LocalDate.parse(memoDto.getScheduleDateStr()).atStartOfDay();
        memoDto.setScheduleDate(dt);

        memoService.insertMemo(memoDto);
        LocalDate date = dt.toLocalDate();

        return "redirect:/calendar?year=" + date.getYear() + "&month=" + date.getMonthValue();
    }

    @PostMapping("/memo/update")
    public String updateMemo(@RequestParam(name="num") int num, MemoDto memoDto) {
        LocalDateTime dt = LocalDate.parse(memoDto.getScheduleDateStr()).atStartOfDay();
        memoDto.setScheduleDate(dt);

        memoService.updateMemo(num, memoDto);
        LocalDate date = dt.toLocalDate();

        return "redirect:/calendar?year=" + date.getYear() + "&month=" + date.getMonthValue();
    }

	@PostMapping("/memo/delete")
	public String deletMemo(@RequestParam(name="num") int num, @RequestParam(name="year") int year, @RequestParam(name="month") int month) {
		memoService.deleteMemo(num);

		return "redirect:/calendar?year=" + year + "&month=" + month;
	}
	
	@GetMapping("/memos")
	@ResponseBody
	public List<Memo> getMemos(@RequestParam(name="year") int year, @RequestParam(name="month") int month, HttpSession session) {
	    Users user = (Users) session.getAttribute(Redirect.loginSession);
	    if (user == null) {
	    return Collections.emptyList(); // 혹은 예외 처리
	    }
	    int userNum = user.getNum();
	    return memoService.memoSelectByUserNumAndMonth(userNum, year, month);
	}
	
	@GetMapping("/memo/detail")
	@ResponseBody
	public Map<String, Object> getMemoDetail(@RequestParam("memoNum") int memoNum) {
	    Memo memo = memoService.memoSelectByNum(memoNum);
	    Map<String, Object> result = new HashMap<>();
	    if (memo != null) {
	        result.put("num", memo.getNum());
	        result.put("title", memo.getTitle());
	        result.put("content", memo.getContent());
	    if (memo.getScheduleDate() != null) {
	        result.put("scheduleDate", memo.getScheduleDate().toLocalDate().toString());
	    } else {
	        result.put("scheduleDate", "");
	    }
	        result.put("animalNum", memo.getAnimalNum());
	        
	        Animal animal = animalService.getAnimalByNum(memo.getAnimalNum());
	        result.put("animalName", animal != null ? animal.getAnimalName() : "알 수 없음");
	    }
	    return result;
	}
}  