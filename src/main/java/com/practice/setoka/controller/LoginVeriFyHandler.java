package com.practice.setoka.controller;

import com.practice.setoka.Redirect;

import jakarta.servlet.http.HttpSession;

public class LoginVeriFyHandler {
	
	private static final String Session_Key = "url";
	
	public static void save(HttpSession session, String url)
	{
		session.setAttribute(Session_Key, "redirect:/"+url);
	}
	
	public static String load(HttpSession session)
	{
		String str = (String)session.getAttribute(Session_Key);
		if(str == null) {
			str = Redirect.home;
		}
		session.removeAttribute(Session_Key);
		return str;
	}
	
//	public String loginVerify(HttpSession session) {
//		Users user = (Users)session.getAttribute(Redirect.loginSession);
//		if(user == null) {
//			return Redirect.LoginForm;
//		}
//		String url = (String)session.getAttribute(Redirect.urlSession);
//		session.removeAttribute(Redirect.urlSession);
//		return url;
//	}
}
