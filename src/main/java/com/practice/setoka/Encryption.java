package com.practice.setoka;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encryption 
{
	public static String Encoder(String str)
	{
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder ();
		return encoder.encode(str);
	}
	
	public static boolean Decoder(String encode, String str)
	{
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder ();
		return encoder.matches(str, encode);
	}
}
