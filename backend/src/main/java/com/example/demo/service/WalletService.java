package com.aitasker.backend.service;

import com.aitasker.backend.entity.Transaction;
import com.aitasker.backend.entity.Wallet;
import com.aitasker.backend.repository.TransactionRepository;
import com.aitasker.backend.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public Wallet deposit(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Số tiền nạp phải lớn hơn 0");
        }

        // Tìm ví của user, nếu chưa có thì tạo mới
        Wallet wallet = walletRepository.findByUserId(userId).orElseGet(() -> {
            Wallet newWallet = new Wallet();
            newWallet.setUserId(userId);
            return newWallet;
        });

        // Cộng tiền vào ví
        wallet.setBalance(wallet.getBalance().add(amount));
        wallet.setUpdatedAt(LocalDateTime.now());
        Wallet savedWallet = walletRepository.save(wallet);

        // Lưu lịch sử giao dịch
        Transaction transaction = new Transaction();
        transaction.setWalletId(savedWallet.getId());
        transaction.setAmount(amount);
        transaction.setType("DEPOSIT");
        transaction.setDescription("Nạp tiền vào ví hệ thống");
        transactionRepository.save(transaction);

        return savedWallet;
    }
}