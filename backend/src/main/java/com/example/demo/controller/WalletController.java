package com.aitasker.backend.controller;

import com.aitasker.backend.entity.Wallet;
import com.aitasker.backend.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallet")
@CrossOrigin("*")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    // API: POST /api/wallet/deposit
    @PostMapping("/deposit")
    public ResponseEntity<Wallet> depositFunds(
            @RequestParam Long userId, 
            @RequestParam BigDecimal amount) {
        Wallet updatedWallet = walletService.deposit(userId, amount);
        return ResponseEntity.ok(updatedWallet);
    }
}