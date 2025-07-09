package com.practice.setoka.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.practice.setoka.dao.Memo;
import com.practice.setoka.dto.MemoDto;
import com.practice.setoka.service.MemoService;

@Controller
public class CalendarController {

    @Autowired
    private MemoService memoService;

    @GetMapping("/calendar")
    public String calendar(Model model,
            @RequestParam(name = "year", required = false) Integer year,
            @RequestParam(name = "month", required = false) Integer month) {

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

        // 해당 월에 있는 메모 리스트 (date 기준)
        List<Memo> memoList = memoService.memoSelectByMonth(year, month);
        model.addAttribute("memoList", memoList);

        return "calendar";
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
	public List<Memo> getMemos(@RequestParam(name="year") int year, @RequestParam(name="month") int month) {
	    return memoService.memoSelectByMonth(year, month);
	}
	
	@GetMapping("/memo/detail")
	@ResponseBody
	public Memo getMemoDetail(@RequestParam("memoNum") int memoNum) {
	    return memoService.memoSelectByNum(memoNum);
	}
}  