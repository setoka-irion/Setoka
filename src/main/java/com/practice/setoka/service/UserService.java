package com.practice.setoka.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.practice.setoka.Redirect;
import com.practice.setoka.Upload;
import com.practice.setoka.Enum.Privileges;
import com.practice.setoka.Enum.Status;
import com.practice.setoka.dao.Users;
import com.practice.setoka.dto.UsersDto;
import com.practice.setoka.mapper.UserMapper;
import com.practice.setoka.springSecurity.CustomUserDetails;

@Service
public class UserService {
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private Upload upload;

	// num 으로 유저 리턴
	public Users selectUser(int num) {
		return userMapper.selectUserByNum(num);
	}

	// id로 유저 리턴
	public Users selectByID(String id) {
		return userMapper.selectUserByID(id);
	}

	// id password로 유저 리턴
	public Users selectByIdPassword(String id, String password) {
		return userMapper.selectUserByIDPassword(id, password);
	}
	
	public Users selectByNickName(String nickName)
	{
		return userMapper.selectByNickName(nickName);
	}

	// 이름 중복검사 (이미 있는 이름이면 reutrn true; 삭제된 아이디의 경우는 false)
	public boolean existsByName(String id) {
		Users user = selectByID(id);
		return user != null && user.getStatus() == Status.정상;
	}

	// 비밀번호 조건 검사 (조건을 만족하면 return true)
	public boolean passwordInvalid(String password) {
		return password != null && password.matches(Redirect.passwordInvalidPattern);
	}

	// 유저 넣기
	public boolean insertUserNomal(UsersDto dto)
	{
		dto.setProfilePath(upload.DefaultProfile());
		if(selectByID(dto.getId()) == null)
		{
			if(selectByNickName(dto.getNickName()) == null)
				return userMapper.insertUserToDto(dto);	
		}
		else
		{
			if(selectByNickName(dto.getNickName()) == null)
			{
				dto.setStatus(Status.정상);
				return userMapper.updateUser(dto);
			}
		}
		
		return false;
	}

	// 어드민 넣기
	public boolean insertUserAdmin(UsersDto dto) {
		dto.setPrivileges(Privileges.관리자);
		return userMapper.insertUserToDto(dto);
	}

	// 모든 유저 가져오기
	public List<Users> selectAllUsers() {
		return userMapper.selectAllUsers();
	}

	// update
	public boolean updateUserDto(UsersDto dto) {
		dto.getExp();
		return userMapper.updateUser(dto);
	}

	// 유저의 프로필 사진을 입력하는 서비스 (회원가입시에 프로필 사진을 정하지 않으니)
	public boolean insertProfilephoto(MultipartFile file, UsersDto dto) {
		return insertProfilephoto(file, dto.getId());
	}

	// 유저의 프로필 사진을 입력하는 서비스 (회원가입시에 프로필 사진을 정하지 않으니)
	public boolean insertProfilephoto(MultipartFile file, String id) {
		String fileName = upload.imageFileUpload(file);
		userMapper.updateProfilephotoPath(fileName, id);

		return true;
	}

	public boolean insertProfilephotoDefalut(String id) {
		String fileName = upload.imagePath + "defaultProfile.png";
		userMapper.updateProfilephotoPath(fileName, id);

		return true;
	}

	// 유저의 프로필 사진 경로를 가져오기
	public String selectProfilePath(UsersDto dto) {
		return selectProfilePath(dto.getId());
	}

	public String selectProfilePath(String id) {
		return userMapper.selectProfilephotoPath(id);
	}

	public boolean userPointUpdate(String id, int point) {
		return userMapper.userPointUpdate(id, point);
	}

	public boolean userSendPoint(Users sender, String reciver, int point) {
		if (!sender.isAdmin() && sender.getPoint() < point)
		{
			point = sender.getPoint();
		}
		
		sender.setPoint(sender.getPoint() - point);

		return userPointUpdate(sender.getId(), sender.getPoint());
	}
	
	public void userUpdate(Users updatedUser) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Authentication newAuth = new UsernamePasswordAuthenticationToken(
		        new CustomUserDetails(updatedUser), // 업데이트된 사용자 객체
		        auth.getCredentials(),
		        auth.getAuthorities()
		);
		SecurityContextHolder.getContext().setAuthentication(newAuth);
	}
}
