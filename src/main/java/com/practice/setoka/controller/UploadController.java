package com.practice.setoka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.practice.setoka.Upload;
import com.practice.setoka.dto.TempImageDto;
import com.practice.setoka.service.BoardService;

@RestController
public class UploadController {

	@Autowired
	private Upload upload;
	@Autowired
	private BoardService boardService;

	@PostMapping("/upload/image")
	public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file,
			@RequestParam("userNum") int userNum){
		String fileName = upload.tempImageUpload(file);;
		String imageUrl = "/" +  fileName;
		//db에 저장
		boardService.insertTempImage(new TempImageDto(userNum, fileName));
		System.out.println(userNum);
		System.out.println("이미지 업로드 실행 url:"+ imageUrl);
		
		return ResponseEntity.ok(imageUrl);
	}

}
