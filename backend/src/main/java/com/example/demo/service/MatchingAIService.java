package com.example.demo.service;

import com.example.demo.dto.ExpertResult;
import com.example.demo.entity.ExpertProfile;
import com.example.demo.repository.ExpertRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.demo.dto.*;
import java.util.*;

@Service
public class MatchingAIService {

    @Autowired private ExpertRepository expertRepo;
    private final String API_KEY = "YOUR_API_KEY"; // Nên dùng @Value("${ai.api.key}")
    private final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;

    public List<ExpertResult> matchExperts(String jobDescription) {
        List<ExpertProfile> experts = expertRepo.findAll();
        String prompt = "Dựa trên job: " + jobDescription + ". Expert: " + experts.toString() + 
                        ". Trả về JSON list [{\"id\": 1, \"matchScore\": 90, \"reasoning\": \"...\"}]";

        GeminiRequest request = new GeminiRequest(prompt);
        RestTemplate restTemplate = new RestTemplate();
        
        // Gọi API
        ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, request, Map.class);
        
        try {
            // Gemini trả về cấu trúc lồng nhau: candidates -> content -> parts -> text
            List<Map> candidates = (List<Map>) response.getBody().get("candidates");
            Map content = (Map) candidates.get(0).get("content");
            List<Map> parts = (List<Map>) content.get("parts");
            String jsonText = (String) parts.get(0).get("text");

            // Loại bỏ các ký tự thừa như ```json ... ``` nếu AI trả về markdown
            jsonText = jsonText.replace("```json", "").replace("```", "").trim();

            // Chuyển String JSON thành List<ExpertResult>
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonText, new TypeReference<List<ExpertResult>>() {});
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}