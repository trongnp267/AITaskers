package com.aitasker.backend.controller;

import com.aitasker.backend.entity.Milestone;
import com.aitasker.backend.service.MilestoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/milestones")
@CrossOrigin("*") 
@RequiredArgsConstructor
public class MilestoneController {

    private final MilestoneService milestoneService;

    
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Milestone>> getMilestones(@PathVariable Long projectId) {
        return ResponseEntity.ok(milestoneService.getMilestonesByProjectId(projectId));
    }

    
    @PostMapping("/{id}/submit")
    public ResponseEntity<Milestone> submitDeliverable(@PathVariable("id") Long milestoneId) {
        Milestone updatedMilestone = milestoneService.submitMilestone(milestoneId);
        return ResponseEntity.ok(updatedMilestone);
    }
}