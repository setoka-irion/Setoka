//package com.practice.setoka.controller;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import jakarta.servlet.http.HttpServletRequest;
//import java.nio.file.Path;
//
//@RestController
//public class UploadController {
//
//    @Value("${upload.image.path}")  // application.properties에 정의할 경로
//    private String uploadDir;
//
//    @PostMapping("/uploadImage")
//    public String uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
//        
//    	// 1. 원본 파일명 얻기
//        String originalFilename = file.getOriginalFilename();
//
//        // 2. 확장자만 추출
//        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
//
//        // 3. UUID 생성하여 중복방지용 파일명 만들기
//        String savedName = UUID.randomUUID().toString() + ext;
//
//        // 4. 업로드 폴더 경로 생성
//        File uploadPath = new File(uploadDir);
//        if (!uploadPath.exists()) uploadPath.mkdirs();
//
//        // 5. 실제 파일 저장
//        File dest = new File(uploadPath, savedName);
//        file.transferTo(dest);
//
//        // 6. 클라이언트가 접근 가능한 URL 리턴
//        return "/upload/boardImages/" + savedName;
//    }
//}
////    
////    @PostMapping("/upload")
////    @ResponseBody
////    public ResponseEntity<Map<String, Object>> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
////        String originalName = file.getOriginalFilename();
////        String savedName = UUID.randomUUID() + "_" + originalName;
////        Path targetPath = Paths.get("업로드_폴더", savedName);
////        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
////
////        // 파일 내용 읽기 (예: JSON)
////        String content = Files.readString(targetPath, StandardCharsets.UTF_8);
////
////        Map<String, Object> response = new HashMap<>();
////        response.put("fileName", savedName);
////        response.put("content", content); // JSON 내용도 포함
////        return ResponseEntity.ok(response);
////    }
////}
