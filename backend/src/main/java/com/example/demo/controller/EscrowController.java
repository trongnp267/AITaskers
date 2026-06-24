package com.aitasker.backend.controller;

import com.aitasker.backend.entity.Escrow;
import com.aitasker.backend.service.EscrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/escrow")
@CrossOrigin("*")
@RequiredArgsConstructor
public class EscrowController {

    private final EscrowService escrowService;

    // API: POST /api/escrow/create
    @PostMapping("/create")
    public ResponseEntity<Escrow> createEscrow(
            @RequestParam Long clientId,
            @RequestParam Long projectId,
            @RequestParam BigDecimal amount) {
        Escrow newEscrow = escrowService.createEscrow(clientId, projectId, amount);
        return ResponseEntity.ok(newEscrow);
    }
    
    // (Giữ nguyên API release cũ...)
}