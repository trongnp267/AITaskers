package com.aitasker.backend.controller;

import com.aitasker.backend.entity.Wallet;
import com.aitasker.backend.repository.WalletRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletRepository walletRepository;

    public WalletController(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> mockDeposit(@RequestParam Long userId, @RequestParam BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví người dùng"));
        
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
        
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "balance", wallet.getBalance()
        ));
    }
}