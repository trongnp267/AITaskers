package com.example.demo.controller;

import com.example.demo.dto.ExpertResult;
import com.example.demo.service.MatchingAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
}