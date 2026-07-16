package com.aitasker.backend.controller;

import com.aitasker.backend.entity.Transaction;
import com.aitasker.backend.entity.Wallet;
import com.aitasker.backend.repository.TransactionRepository;
import com.aitasker.backend.repository.WalletRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public WalletController(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getWallet(@PathVariable Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví người dùng"));
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "balance", wallet.getBalance()
        ));
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> mockDeposit(@RequestParam Long userId, @RequestParam BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví người dùng"));

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);

        Transaction depositTx = new Transaction();
        depositTx.setWallet(wallet);
        depositTx.setAmount(amount);
        depositTx.setTransactionType("DEPOSIT");
        transactionRepository.save(depositTx);

        return ResponseEntity.ok(Map.of(
            "status", "success",
            "balance", wallet.getBalance()
        ));
    }
}
