package com.practice.setoka;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;

public class JsonConverter implements AttributeConverter<Map<String, Object>,String> {
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public String convertToDatabaseColumn(Map<String,Object> attribute) {
		try { 
			return objectMapper.writeValueAsString(attribute);
			
		}catch (Exception e) {
			throw new RuntimeException("JSON 직렬화 실패", e);
		}
		
	}
	@Override
	public Map<String,Object> convertToEntityAttribute(String dbData){
		try {
			return objectMapper.readValue(dbData, Map.class);
			
		}catch (Exception e) {
			throw new RuntimeException("JSON 역직렬화 실패", e);
		}
	}
}
