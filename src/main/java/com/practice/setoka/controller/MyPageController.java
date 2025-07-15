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

import com.practice.setoka.Encryption;
import com.practice.setoka.Redirect;
import com.practice.setoka.Enum.Status;
import com.practice.setoka.dao.Animal;
import com.practice.setoka.dao.Memo;
import com.practice.setoka.dao.Users;
import com.practice.setoka.dto.MemoDto;
import com.practice.setoka.dto.UsersDto;
import com.practice.setoka.service.AnimalService;
import com.practice.setoka.service.MemoService;
import com.practice.setoka.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MyPageController {

	@Autowired
	private UserService userService;
	@Autowired
	private AnimalService animalService;
	@Autowired
    private MemoService memoService;

	// 마이페이지
	@GetMapping(value = "MyPage")
	public String myPage(HttpSession session,
						Model model,
						@RequestParam(name = "year", required = false)Integer year,
						@RequestParam(name = "month", required = false)Integer month) {
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		if (user == null) {
			SessionUrlHandler.save(session, "MyPage");
			return Redirect.LoginForm;
		}
		
		//달력
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
		return "MyPage";
	}

	// 비밀번호 재확인
	@GetMapping(value = "PasswordConfirm")
	public String passwordConfirmForm(HttpSession session) {
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		if (user == null) {
			SessionUrlHandler.save(session, "MyPage");
			return Redirect.LoginForm;
		}
		return "PasswordConfirm";
	}

	@PostMapping(value = "PasswordConfirm")
	public String passwordConfirmSubmit(@RequestParam("password")String password, HttpSession session) {
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		if (Encryption.Decoder(user.getPassword(), password)) {
			String url = SessionUrlHandler.load(session);
			session.setAttribute("PasswordConfirmed", "PasswordConfirmed");
			return url;
		}
		return Redirect.passwordConfirm;
	}

	// 개인정보수정
	@GetMapping(value = "ModifyUser")
	public String modifyUser(HttpSession session, Model model) {
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		if (user == null) {
			return Redirect.home;
		}

		String passwordConfirm = (String) session.getAttribute("PasswordConfirmed");
		if (passwordConfirm == null) {
			SessionUrlHandler.save(session, "ModifyUser");
			return Redirect.passwordConfirm;
		}
		session.removeAttribute("PasswordConfirmed");

		UsersDto dto = new UsersDto(user);
		// html을 꾸며줄 클래스 넣기
		model.addAttribute("UsersDto", dto);
		// 수정 페이지로 이동
		return "ModifyUser";
	}

	@PostMapping(value = "ModifyUser")
	public String modifyUserPost(HttpSession session, Model model, UsersDto userDto) {
		// 로그인 되어 있는 사람의 정보
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		// 수정될 정보
		if (userService.updateUserDto(userDto)) {
			// 정보 수정 성공
			session.removeAttribute(Redirect.loginSession);
			user = userService.selectByID(userDto.getId());
			session.setAttribute(Redirect.loginSession, user);
		} else {
			// 정보 수정 실패
		}
		// 정보 수정
		return Redirect.MyPage;
	}

	// 비밀번호 변경
	@GetMapping(value = "ChangePassword")
	public String changePassword(HttpSession session) {
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		if (user == null) {
			return Redirect.home;
		}
		
		String passwordConfirm = (String) session.getAttribute("PasswordConfirmed");
		if (passwordConfirm == null) {
			SessionUrlHandler.save(session, "ChangePassword");
			return Redirect.passwordConfirm;
		}
		session.removeAttribute("PasswordConfirmed");
		
		return "ChangePassword";
	}
	
	@PostMapping(value = "ChangePassword")
	public String changePasswordsubmit(@RequestParam("password")String password,
									@RequestParam("passwordCon")String passwordCon,
									HttpSession session) {
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		String encordPassowrd = Encryption.Encoder(password);
		if (Encryption.Decoder(encordPassowrd, passwordCon) && !Encryption.Decoder(user.getPassword(), passwordCon)) {
			UsersDto dto = new UsersDto(user);
			dto.setPassword(encordPassowrd);
			userService.updateUserDto(dto);
			session.removeAttribute(Redirect.loginSession);
			return Redirect.home;
		}
		return Redirect.changePassword;
	}
	
	// 동물프로필
	@GetMapping(value = "AnimalProfile")
	public String animalProfile() {
		return "AnimalProfile";
	}

	// 프로필 추가
	@GetMapping(value = "AddProfile")
	public String addProfile() {
		return "AddProfile";
	}

	// 다마
	@GetMapping(value = "Damagochi")
	public String damagochi() {
		return "Damagochi";
	}

	// 탈퇴
	@GetMapping(value = "Withdrawal")
	public String withdrawal(HttpSession session) {
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		if (user == null) {
			return Redirect.home;
		}
		return "Withdrawal";
	}
	
	@PostMapping(value = "Withdrawal")
	public String withdrawalSubmit(@RequestParam("password")String password, HttpSession session) {
		Users user = (Users) session.getAttribute(Redirect.loginSession);
		if(Encryption.Decoder(user.getPassword(), password)) {
			UsersDto dto = new UsersDto(user);
			dto.setStatus(Status.삭제);
			userService.updateUserDto(dto);
			session.removeAttribute(Redirect.loginSession);
			return Redirect.home;
		}
		return Redirect.withdrawal;
	}
	
	//달력 스크립트 이용 기능들
	@PostMapping("/memo/add")
    public String addMemo(MemoDto memoDto) {
        LocalDateTime dt = LocalDate.parse(memoDto.getScheduleDateStr()).atStartOfDay();
        memoDto.setScheduleDate(dt);

        memoService.insertMemo(memoDto);
        LocalDate date = dt.toLocalDate();

        return "redirect:/MyPage?year=" + date.getYear() + "&month=" + date.getMonthValue();
    }

    @PostMapping("/memo/update")
    public String updateMemo(@RequestParam(name="num")int num,
    						MemoDto memoDto) {
        LocalDateTime dt = LocalDate.parse(memoDto.getScheduleDateStr()).atStartOfDay();
        memoDto.setScheduleDate(dt);

        memoService.updateMemo(num, memoDto);
        LocalDate date = dt.toLocalDate();

        return "redirect:/MyPage?year=" + date.getYear() + "&month=" + date.getMonthValue();
    }

	@PostMapping("/memo/delete")
	public String deletMemo(@RequestParam(name="num")int num,
							@RequestParam(name="year")int year,
							@RequestParam(name="month")int month) {
		memoService.deleteMemo(num);

		return "redirect:/MyPage?year=" + year + "&month=" + month;
	}
	
	@GetMapping("/memos")
	@ResponseBody
	public List<Memo> getMemos(@RequestParam(name="year")int year,
							@RequestParam(name="month")int month,
							HttpSession session) {
	    Users user = (Users) session.getAttribute(Redirect.loginSession);
	    if (user == null) {
	    return Collections.emptyList(); // 혹은 예외 처리
	    }
	    int userNum = user.getNum();
	    return memoService.memoSelectByUserNumAndMonth(userNum, year, month);
	}
	
	@GetMapping("/memo/detail")
	@ResponseBody
	public Map<String, Object> getMemoDetail(@RequestParam("memoNum")int memoNum) {
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
