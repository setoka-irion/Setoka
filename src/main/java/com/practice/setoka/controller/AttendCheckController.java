package com.practice.setoka.controller;

import java.time.LocalDate;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.practice.setoka.Redirect;
import com.practice.setoka.dao.Animal;
import com.practice.setoka.dao.Users;
import com.practice.setoka.service.AnimalService;
import com.practice.setoka.service.AttendCheckService;
import com.practice.setoka.springSecurity.CustomUserDetails;
import com.practice.setoka.springSecurity.CustomUserDetailsService;

@Controller
public class AttendCheckController {

	@Autowired
	private AttendCheckService attendCheckService;

	@Autowired
	private AnimalService animalService;

	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@GetMapping("/attendcheck")
	public String calendar(Model model, @RequestParam(name = "year", required = false) Integer year,
			@RequestParam(name = "month", required = false) Integer month, @AuthenticationPrincipal CustomUserDetails authUser) {

		Users users = (Users) authUser.getUser();
		int userNum = users.getNum();
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

		return "AttendCheck";
	}

	@PostMapping("/point")
	public String attendPoint(@RequestParam(name = "date", required = false) String date,
			RedirectAttributes redirectAttributes, @AuthenticationPrincipal CustomUserDetails authUser) {
		
		var users = (Users) authUser.getUser();
		int userNum = users.getNum();
		LocalDate today = LocalDate.now();
		LocalDate clickedDate = LocalDate.parse(date);

		// 같은 날만 출석 체크 가능
		if (!clickedDate.equals(today)) {
			redirectAttributes.addFlashAttribute("message", "다른 날은 출석할 수 없습니다.");
		} else {
			users.setPoint(users.getPoint()+100);
			attendCheckService.updatePoint(userNum);
			attendCheckService.insertAttendance(userNum, date);
			
			redirectAttributes.addFlashAttribute("newAttendance", date);
			redirectAttributes.addFlashAttribute("message", "출석체크가 완료되었습니다.");
		}

		return Redirect.attendcheck;
	}

	@GetMapping("/getDate")
	@ResponseBody
	public List<String> attendanceDates(@AuthenticationPrincipal CustomUserDetails authUser) {
		var users = (Users) authUser.getUser();		
		int userNum = users.getNum();

		List<String> attendanceDates = attendCheckService.getAttendanceDate(userNum);
		return attendanceDates;
	}
}