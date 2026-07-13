package com.aitasker.backend.controller;

import com.aitasker.backend.entity.Milestone;
import com.aitasker.backend.service.MilestoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @PostMapping("/{id}/submit")
    public ResponseEntity<Milestone> submitDeliverable(@PathVariable("id") Long milestoneId) {
        Milestone updatedMilestone = milestoneService.submitMilestone(milestoneId);
        return ResponseEntity.ok(updatedMilestone);
    }

    // TRUOC DAY: chi co endpoint submit, khong co endpoint nao de Client duyet
    // hoac tu choi san pham -> tien trong Escrow khong bao gio duoc giai ngan
    // sang vi Expert. Bo sung 2 endpoint con thieu nay.
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
