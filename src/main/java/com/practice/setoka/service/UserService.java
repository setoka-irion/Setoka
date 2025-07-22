package com.practice.setoka.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.practice.setoka.Redirect;
import com.practice.setoka.Upload;
import com.practice.setoka.Enum.Grade;
import com.practice.setoka.Enum.Status;
import com.practice.setoka.dao.Users;
import com.practice.setoka.dto.UsersDto;
import com.practice.setoka.mapper.UserMapper;


@Service
public class UserService 
{
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private Upload upload;
	
	//num 으로 유저 리턴
	public Users selectUser(int num)
	{
		return userMapper.selectUserByNum(num);
	}
	
	//id로 유저 리턴
	public Users selectByID(String id)
	{
		return userMapper.selectUserByID(id);
	}
	
	//id password로 유저 리턴
	public Users selectByIdPassword(String id, String password)
	{
		return userMapper.selectUserByIDPassword(id, password);
	}
	
	//이름 중복검사 (이미 있는 이름이면 reutrn true; 삭제된 아이디의 경우는 false)
	public boolean existsByName(String id)
	{
		Users user = selectByID(id);
		return user != null && user.getStatus() == Status.정상;
	}
	
	//비밀번호 조건 검사 (조건을 만족하면 return true)
	public boolean passwordInvalid(String password)
	{
		return password != null && password.matches(Redirect.passwordInvalidPattern);
	}
	
	//유저 넣기
	public boolean insertUserNomal(UsersDto dto)
	{
		if(selectByID(dto.getId()) == null)
			return userMapper.insertUserToDto(dto);
		else
			return userMapper.updateUser(dto);
	}
	
	//어드민 넣기
	public boolean insertUserAdmin(UsersDto dto)
	{
		dto.setGrade(Grade.관리자);
		return userMapper.insertUserToDto(dto);
	}
	
	//모든 유저 가져오기
	public List<Users> selectAllUsers(){
		return userMapper.selectAllUsers();
	}
	
	//update
	public boolean updateUserDto(UsersDto dto)
	{
		return userMapper.updateUser(dto);
	}
	
	//유저가 존재하면서 삭제가 아닌 경우에만 리턴 나머지는 null 리턴
	public Users loginUser(UsersDto dto)
	{
		Users user = selectByID(dto.getId());
		if(user == null)
			return null;
		
		if(user.getStatus().equals(Status.삭제))
			return null;
		
		return user;
	}

	//유저의 프로필 사진을 입력하는 서비스 (회원가입시에 프로필 사진을 정하지 않으니)
	public boolean insertProfilephoto(MultipartFile file, UsersDto dto)
	{
		return insertProfilephoto(file, dto.getId());
	}
	
	//유저의 프로필 사진을 입력하는 서비스 (회원가입시에 프로필 사진을 정하지 않으니)
	public boolean insertProfilephoto(MultipartFile file, String id)
	{
		String fileName = upload.fileUpload(file);
		userMapper.updateProfilephotoPath(fileName, id);
		
		return true;
	}
	
	//유저의 프로필 사진 경로를 가져오기
	public String selectProfilePath(UsersDto dto)
	{
		return selectProfilePath(dto.getId());
	}
	public String selectProfilePath(String id)
	{
		return userMapper.selectProfilephotoPath(id);
	}
}
