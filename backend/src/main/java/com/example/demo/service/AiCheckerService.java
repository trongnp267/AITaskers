package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class AiCheckerService {

    private final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";
    
    private final String GEMINI_API_KEY = "AQ.Ab8RN6L4qE_PqthilrVFiDZmcawnZZgVn_l1Do-jAROkEYDIkA"; 

    public Map<String, Object> checkExpertProfile(String skill, String experience, String certificate, Double hourlyRate) {
        Map<String, Object> result = new HashMap<>();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String prompt = String.format(
                "Analyze and rate this expert profile: Skill: %s, Experience: %s, Certificate: %s, Hourly Rate: $%s. " +
                "Respond ONLY in valid JSON format: {\"score\": integer_0_to_100, \"feedback\": \"string\"}",
                skill, experience, certificate, hourlyRate
            );

            Map<String, Object> body = new HashMap<>();
            List<Map<String, Object>> contents = new ArrayList<>();
            Map<String, Object> partMap = new HashMap<>();
            partMap.put("text", prompt);
            contents.add(Map.of("parts", List.of(partMap)));
            body.put("contents", contents);
            body.put("generationConfig", Map.of("responseMimeType", "application/json"));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL + GEMINI_API_KEY, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                String text = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
                JsonNode json = mapper.readTree(text.replaceAll("```json", "").replaceAll("```", ""));
                
                result.put("score", json.path("score").asInt(60));
                result.put("feedback", json.path("feedback").asText("Profile requires manual review."));
            }
        } catch (Exception e) {
            System.err.println("AI Error: " + e.getMessage());
            result.put("score", 60);
            result.put("feedback", "AI temporarily unavailable.");
        }
        return result;
    }
}