package com.practice.setoka;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonFileWriter 
{
	public static String saveJsonToFile(Map<String, Object> jsonString, String filePath, String fileName) 
	{
        try 
        {
            String uuid = UUID.randomUUID().toString();
            fileName = uuid + "_" + fileName + ".json";
            
            Path path = Paths.get(filePath + "/" + fileName);

            // 디렉토리 없으면 생성
            Files.createDirectories(path.getParent());

            ObjectMapper mapper = new ObjectMapper();
            String prettyJson = mapper.writeValueAsString(jsonString);
            // JSON 문자열 파일에 쓰기
            Files.write(path, prettyJson.getBytes(StandardCharsets.UTF_8));	

            return fileName;
        } 
        catch (IOException e) 
        {
            System.err.println("❌ JSON 파일 저장 실패: " + e.getMessage());
        }
        return null;
    }
	
	public static Map<String, Object> readJsonFileToMap(String filePath) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Path path = Path.of(filePath);

        // 파일 내용을 Map으로 바로 변환
        return mapper.readValue(path.toFile(), Map.class);
    }
}
