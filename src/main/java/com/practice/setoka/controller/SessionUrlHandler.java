package com.practice.setoka.controller;

import com.practice.setoka.Redirect;

import jakarta.servlet.http.HttpSession;

public class SessionUrlHandler {
	
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
}
