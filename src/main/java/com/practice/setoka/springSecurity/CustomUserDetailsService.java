package com.practice.setoka.springSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.practice.setoka.dao.Users;
import com.practice.setoka.mapper.UserMapper;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = userMapper.selectUserByID(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found: " + username);
		}
		return new CustomUserDetails(user);
	}
}
