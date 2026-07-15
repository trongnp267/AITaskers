package com.aitasker.backend.controller;

import com.aitasker.backend.dto.MilestoneRequest;
import com.aitasker.backend.entity.Milestone;
import com.aitasker.backend.service.MilestoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
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

    @PostMapping
    public ResponseEntity<?> createMilestone(@RequestBody MilestoneRequest request) {
        try {
            Milestone created = milestoneService.createMilestone(request);
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Tao milestone thanh cong.",
                "data", created
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", e.getMessage()
            ));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMilestone(@PathVariable("id") Long milestoneId,
                                             @RequestBody MilestoneRequest request) {
        try {
            Milestone updated = milestoneService.updateMilestone(milestoneId, request);
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Cap nhat milestone thanh cong.",
                "data", updated
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<Milestone> submitDeliverable(@PathVariable("id") Long milestoneId) {
        Milestone updatedMilestone = milestoneService.submitMilestone(milestoneId);
        return ResponseEntity.ok(updatedMilestone);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Milestone> approveDeliverable(@PathVariable("id") Long milestoneId) {
        Milestone updatedMilestone = milestoneService.approveMilestone(milestoneId);
        return ResponseEntity.ok(updatedMilestone);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Milestone> rejectDeliverable(@PathVariable("id") Long milestoneId) {
        Milestone updatedMilestone = milestoneService.rejectMilestone(milestoneId);
        return ResponseEntity.ok(updatedMilestone);
    }
}
