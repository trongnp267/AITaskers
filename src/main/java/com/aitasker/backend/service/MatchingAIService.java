package com.aitasker.backend.service;

import com.aitasker.backend.dto.ExpertResult;
import com.aitasker.backend.dto.GeminiRequest;
import com.aitasker.backend.entity.ExpertProfile;
import com.aitasker.backend.entity.Job;
import com.aitasker.backend.entity.JobSkill;
import com.aitasker.backend.exception.BadRequestException;
import com.aitasker.backend.exception.ResourceNotFoundException;
import com.aitasker.backend.repository.ExpertRepository;
import com.aitasker.backend.repository.JobRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MatchingAIService {

    @Autowired
    private ExpertRepository expertRepo;

    @Autowired
    private JobRepository jobRepository;

    @Value("${ai.gemini.api-key}")
    private String apiKey;

    @Value("${ai.gemini.api-url}")
    private String apiUrl;

    private final ObjectMapper mapper = new ObjectMapper();

    public List<ExpertResult> matchExperts(String jobDescription) {
        List<ExpertProfile> experts = expertRepo.findAll();
        String prompt = "Dua tren job: " + jobDescription + ". Expert: " + experts.toString() +
                        ". Tra ve JSON list [{\"id\": 1, \"matchScore\": 90, \"reasoning\": \"...\"}]";

        String jsonText = callGemini(prompt);
        if (jsonText == null) {
            return new ArrayList<>();
        }
        try {
            return mapper.readValue(cleanJson(jsonText), new TypeReference<List<ExpertResult>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<ExpertResult> matchExpertsForJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        return matchExperts(buildJobDescription(job));
    }

    public Map<String, Object> analyzeJobDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new BadRequestException("Job description is required");
        }

        String prompt = "Phan tich mo ta cong viec sau va tra ve JSON dang " +
                "{\"summary\": \"tom tat ngan\", \"suggestedSkills\": [\"...\"], " +
                "\"estimatedComplexity\": \"LOW|MEDIUM|HIGH\", " +
                "\"recommendedBudgetMin\": 0, \"recommendedBudgetMax\": 0}. Mo ta: " + description;

        String jsonText = callGemini(prompt);
        if (jsonText != null) {
            try {
                return mapper.readValue(cleanJson(jsonText), new TypeReference<Map<String, Object>>() {});
            } catch (Exception ignored) {
                // roi xuong fallback ben duoi
            }
        }

        Map<String, Object> fallback = new HashMap<>();
        fallback.put("aiEnabled", false);
        fallback.put("summary", "Khong the phan tich bang AI (chua cau hinh Gemini API key hoac loi ket noi).");
        fallback.put("suggestedSkills", new ArrayList<>());
        fallback.put("estimatedComplexity", "UNKNOWN");
        return fallback;
    }

    private String buildJobDescription(Job job) {
        String skills = job.getJobSkills() == null ? "" : job.getJobSkills().stream()
                .map(JobSkill::getSkill)
                .filter(s -> s != null)
                .map(s -> s.getSkillName())
                .collect(Collectors.joining(", "));

        return "Title: " + job.getTitle()
                + ". Description: " + job.getDescription()
                + ". Position: " + job.getPositionRequirement()
                + ". Skills: " + skills
                + ". Budget: " + job.getBudgetMin() + "-" + job.getBudgetMax();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private String callGemini(String prompt) {
        try {
            GeminiRequest request = new GeminiRequest(prompt);
            RestTemplate restTemplate = new RestTemplate();
            String url = apiUrl + "?key=" + apiKey;

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            if (response.getBody() == null) {
                return null;
            }

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
            if (candidates == null || candidates.isEmpty()) {
                return null;
            }

            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            return null;
        }
    }

    private String cleanJson(String jsonText) {
        return jsonText.replace("```json", "").replace("```", "").trim();
    }
}
