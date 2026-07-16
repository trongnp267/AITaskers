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
        if (experts.isEmpty()) {
            return new ArrayList<>();
        }

        String expertList = experts.stream()
                .map(e -> String.format(
                        "{\"id\": %d, \"ten\": \"%s\", \"moTa\": \"%s\", \"namKinhNghiem\": %s, \"rating\": %s, \"soJobHoanThanh\": %s}",
                        e.getExpertId(),
                        e.getUser() != null ? e.getUser().getUsername() : "?",
                        e.getDescription() != null ? e.getDescription().replace("\"", "'") : "",
                        e.getExperienceYears(),
                        e.getRating(),
                        e.getCompletedJobs()))
                .collect(Collectors.joining(", "));

        String prompt = "Danh sach chuyen gia: [" + expertList + "]. "
                + "Cong viec can tuyen: " + jobDescription + ". "
                + "Cham diem do phu hop (0-100) cua TUNG chuyen gia voi cong viec nay. "
                + "CHI tra ve mot JSON array thuan tuy (khong markdown, khong giai thich them) theo dung dang: "
                + "[{\"id\": 1, \"matchScore\": 90, \"reasoning\": \"ly do ngan gon bang tieng Viet\"}]";

        String jsonText = callGemini(prompt);
        if (jsonText == null) {
            return new ArrayList<>();
        }
        try {
            return mapper.readValue(extractJsonArray(jsonText), new TypeReference<List<ExpertResult>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<ExpertResult> matchExpertsForJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy công việc"));
        return matchExperts(buildJobDescription(job));
    }

    public Map<String, Object> analyzeJobDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new BadRequestException("Thiếu mô tả công việc");
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
            }
        }

        Map<String, Object> fallback = new HashMap<>();
        fallback.put("aiEnabled", false);
        fallback.put("summary", "Không thể phân tích bằng AI (chưa cấu hình Gemini API key hoặc lỗi kết nối).");
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

    private String extractJsonArray(String text) {
        String cleaned = cleanJson(text);
        int start = cleaned.indexOf('[');
        int end = cleaned.lastIndexOf(']');
        if (start >= 0 && end > start) {
            return cleaned.substring(start, end + 1);
        }
        return cleaned;
    }
}
