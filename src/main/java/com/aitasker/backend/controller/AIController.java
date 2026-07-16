package com.aitasker.backend.controller;

import com.aitasker.backend.dto.ExpertResult;
import com.aitasker.backend.service.MatchingAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private MatchingAIService matchingAIService;

    @PostMapping("/match")
    public ResponseEntity<List<ExpertResult>> getRecommendation(@RequestBody String jobDescription) {
        List<ExpertResult> results = matchingAIService.matchExperts(jobDescription);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/match/job/{jobId}")
    public ResponseEntity<List<ExpertResult>> matchForJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(matchingAIService.matchExpertsForJob(jobId));
    }

    @PostMapping("/analyze")
    public ResponseEntity<Map<String, Object>> analyze(@RequestBody Map<String, String> body) {
        String description = body.get("description");
        return ResponseEntity.ok(matchingAIService.analyzeJobDescription(description));
    }
}
