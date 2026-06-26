package com.aitasker.backend.controller;

import com.aitasker.backend.entity.Proposal;
import com.aitasker.backend.repository.ProposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proposals")
public class ProposalController {

    @Autowired
    private ProposalRepository proposalRepository;

    @GetMapping("/job/{id}")
    public ResponseEntity<?> getProposalsByJob(@PathVariable Long id) {
        try {
            List<Proposal> proposals = proposalRepository.findByJobId(id);
            if (proposals.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chưa có Proposal nào cho Job ID: " + id);
            }
            return ResponseEntity.ok(proposals); // Trả về Code 200 xanh lá và data
        } catch (Exception e) {
            // Lỗi 500 sẽ được in rõ lý do ra màn hình Swagger
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
        }
    }
}