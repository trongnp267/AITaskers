package com.aitasker.backend.controller;

import com.aitasker.backend.entity.Transaction;
import com.aitasker.backend.entity.User;
import com.aitasker.backend.entity.Wallet;
import com.aitasker.backend.exception.BadRequestException;
import com.aitasker.backend.exception.ResourceNotFoundException;
import com.aitasker.backend.repository.TransactionRepository;
import com.aitasker.backend.repository.UserRepository;
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
    private final UserRepository userRepository;

    public WalletController(WalletRepository walletRepository,
                            TransactionRepository transactionRepository,
                            UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    private Wallet getOrCreateWallet(Long userId) {
        return walletRepository.findByUserId(userId).orElseGet(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
            Wallet wallet = new Wallet();
            wallet.setUser(user);
            wallet.setBalance(BigDecimal.ZERO);
            return walletRepository.save(wallet);
        });
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getWallet(@PathVariable Long userId) {
        Wallet wallet = getOrCreateWallet(userId);
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "balance", wallet.getBalance()
        ));
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> mockDeposit(@RequestParam Long userId, @RequestParam BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Số tiền nạp phải lớn hơn 0");
        }

        Wallet wallet = getOrCreateWallet(userId);

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);

        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            Transaction depositTx = new Transaction();
            depositTx.setWallet(wallet);
            depositTx.setAmount(amount);
            depositTx.setTransactionType("DEPOSIT");
            transactionRepository.save(depositTx);
        }

        return ResponseEntity.ok(Map.of(
            "status", "success",
            "balance", wallet.getBalance()
        ));
    }
}
