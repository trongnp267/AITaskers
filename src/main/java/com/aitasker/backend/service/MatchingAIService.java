package com.aitasker.backend.service;

import com.aitasker.backend.dto.ExpertResult;
import com.aitasker.backend.dto.GeminiRequest;
import com.aitasker.backend.entity.ExpertProfile;
import com.aitasker.backend.repository.ExpertRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MatchingAIService {

    @Autowired
    private ExpertRepository expertRepo;

    // TRUOC DAY: API_KEY = "YOUR_API_KEY" bi hard-code thang trong code (khong an
    // toan, phai sua code + build lai moi khi doi key). Chuyen sang doc tu
    // application.properties (ai.gemini.api-key).
    @Value("${ai.gemini.api-key}")
    private String apiKey;

    @Value("${ai.gemini.api-url}")
    private String apiUrl;

    public List<ExpertResult> matchExperts(String jobDescription) {
        List<ExpertProfile> experts = expertRepo.findAll();
        String prompt = "Dua tren job: " + jobDescription + ". Expert: " + experts.toString() +
                        ". Tra ve JSON list [{\"id\": 1, \"matchScore\": 90, \"reasoning\": \"...\"}]";

        GeminiRequest request = new GeminiRequest(prompt);
        RestTemplate restTemplate = new RestTemplate();
        String url = apiUrl + "?key=" + apiKey;

        try {
            @SuppressWarnings("rawtypes")
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getBody() == null) {
                return new ArrayList<>();
            }

            // Gemini tra ve cau truc long nhau: candidates -> content -> parts -> text
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
            if (candidates == null || candidates.isEmpty()) {
                return new ArrayList<>();
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            String jsonText = (String) parts.get(0).get("text");

            // Loai bo cac ky tu thua nhu ```json ... ``` neu AI tra ve markdown
            jsonText = jsonText.replace("```json", "").replace("```", "").trim();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonText, new TypeReference<List<ExpertResult>>() {});

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
