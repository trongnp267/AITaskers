package com.aitasker.backend.controller;

import com.aitasker.backend.entity.Escrow;
import com.aitasker.backend.repository.EscrowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/escrow")
public class EscrowController {

    @Autowired
    private EscrowRepository escrowRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createEscrow(@RequestBody Escrow escrow) {
        try {
            Escrow savedEscrow = escrowRepository.save(escrow);
            return ResponseEntity.ok(savedEscrow);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi tạo Escrow: " + e.getMessage());
        }
    }
}
