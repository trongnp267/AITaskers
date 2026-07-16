package com.aitasker.backend.controller;

import com.aitasker.backend.entity.ExpertProfile;
import com.aitasker.backend.exception.ResourceNotFoundException;
import com.aitasker.backend.repository.ExpertProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/experts")
public class ExpertController {

    private final ExpertProfileRepository expertProfileRepository;

    public ExpertController(ExpertProfileRepository expertProfileRepository) {
        this.expertProfileRepository = expertProfileRepository;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllExperts() {
        List<Map<String, Object>> result = expertProfileRepository.findAll()
                .stream().map(this::toResponse).toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getExpert(@PathVariable Long id) {
        ExpertProfile expert = expertProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expert not found"));
        return ResponseEntity.ok(toResponse(expert));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateExpert(@PathVariable Long id,
                                                            @RequestBody Map<String, Object> body) {
        ExpertProfile expert = expertProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expert not found"));

        if (body.get("description") != null) {
            expert.setDescription(String.valueOf(body.get("description")));
        }
        if (body.get("experienceYears") != null) {
            expert.setExperienceYears(Integer.valueOf(String.valueOf(body.get("experienceYears"))));
        }
        expertProfileRepository.save(expert);
        return ResponseEntity.ok(toResponse(expert));
    }

    private Map<String, Object> toResponse(ExpertProfile expert) {
        Map<String, Object> map = new HashMap<>();
        map.put("expertId", expert.getExpertId());
        map.put("username", expert.getUser() != null ? expert.getUser().getUsername() : null);
        map.put("userId", expert.getUser() != null ? expert.getUser().getId() : null);
        map.put("description", expert.getDescription());
        map.put("experienceYears", expert.getExperienceYears());
        map.put("rating", expert.getRating());
        map.put("completedJobs", expert.getCompletedJobs());
        return map;
    }
}
