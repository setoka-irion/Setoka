package com.practice.setoka;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.practice.setoka.dao.Users;
import com.practice.setoka.springSecurity.CustomUserDetails;

@ControllerAdvice
public class GlobalUserAdvice {

	@ModelAttribute("loginUser")
	public Users loginUser(@AuthenticationPrincipal CustomUserDetails authUser) {
		if(authUser!=null) {
			return authUser.getUser();
		}
		return null;
	}
}
