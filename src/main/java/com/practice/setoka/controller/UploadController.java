package com.practice.setoka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.practice.setoka.Upload;

@RestController
public class UploadController {

	@Autowired
	private Upload upload;

	@PostMapping("/upload/image")
	public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file){
		String imageUrl = "/images/" + upload.imageFileUpload(file);
		System.out.println("이미지 업로드 실행 url:"+ imageUrl);
		return ResponseEntity.ok(imageUrl);
	}

}
