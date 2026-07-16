package com.aitasker.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aitasker.backend.dto.ProposalRequest;
import com.aitasker.backend.entity.Proposal;
import com.aitasker.backend.service.ProposalService;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/proposals")
@SecurityRequirement(name = "bearerAuth")
public class ProposalController {

    private final ProposalService proposalService;

    public ProposalController(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    @PostMapping
    public ResponseEntity<?> createProposal(@RequestBody ProposalRequest request) {
        try {
            Proposal created = proposalService.createProposal(request);
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Gui bao gia thanh cong.",
                "data", created
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/{proposalId}/accept")
    public ResponseEntity<?> acceptProposal(@PathVariable Long proposalId) {
        try {
            Proposal accepted = proposalService.acceptProposal(proposalId);
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Chấp nhận báo giá thành công. Tiền đã được chuyển vào quỹ đóng băng bảo mật.",
                "data", accepted
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping
    public List<Proposal> getAllProposals() {
        return proposalService.getAllProposals();
    }

    @GetMapping("/{id}")
    public Proposal getProposalById(@PathVariable Long id) {
        return proposalService.getProposalById(id);
    }

    @GetMapping("/job/{jobId}")
    public List<Proposal> getProposalsByJobId(@PathVariable Long jobId) {
        return proposalService.getProposalsByJobId(jobId);
    }

    @GetMapping("/expert/{expertId}")
    public List<Proposal> getProposalsByExpertId(@PathVariable Long expertId) {
        return proposalService.getProposalsByExpertId(expertId);
    }
}
