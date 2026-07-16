package com.aitasker.backend.controller;

import com.aitasker.backend.entity.Escrow;
import com.aitasker.backend.repository.EscrowRepository;
import com.aitasker.backend.service.EscrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.Map;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/escrow")
public class EscrowController {

    @Autowired
    private EscrowRepository escrowRepository;

    @Autowired
    private EscrowService escrowService;

    @PostMapping("/create")
    public ResponseEntity<?> createEscrow(@RequestBody Escrow escrow) {
        try {
            Escrow savedEscrow = escrowRepository.save(escrow);
            return ResponseEntity.ok(savedEscrow);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi tạo Escrow: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/release")
    public ResponseEntity<?> releaseEscrow(@PathVariable("id") Long escrowId) {
        Escrow escrow = escrowService.releaseEscrow(escrowId);
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "Da giai ngan tien ky quy cho Expert.",
            "data", escrow
        ));
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<?> refundEscrow(@PathVariable("id") Long escrowId) {
        Escrow escrow = escrowService.refundEscrow(escrowId);
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "Da hoan tien ky quy ve cho Client.",
            "data", escrow
        ));
    }
}
