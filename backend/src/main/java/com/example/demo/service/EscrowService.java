package com.aitasker.backend.service;

import com.aitasker.backend.entity.Escrow;
import com.aitasker.backend.entity.Transaction;
import com.aitasker.backend.entity.Wallet;
import com.aitasker.backend.repository.EscrowRepository;
import com.aitasker.backend.repository.TransactionRepository;
import com.aitasker.backend.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class EscrowService {

    private final EscrowRepository escrowRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public Escrow createEscrow(Long clientId, Long projectId, BigDecimal amount) {
        // 1. Kiểm tra ví của Client
        Wallet clientWallet = walletRepository.findByUserId(clientId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví của khách hàng!"));

        // 2. Kiểm tra số dư có đủ để ký quỹ không
        if (clientWallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Số dư trong ví không đủ để ký quỹ dự án này. Vui lòng nạp thêm!");
        }

        // 3. Trừ tiền trong ví Client
        clientWallet.setBalance(clientWallet.getBalance().subtract(amount));
        walletRepository.save(clientWallet);

        // 4. Lưu lịch sử trừ tiền
        Transaction transaction = new Transaction();
        transaction.setWalletId(clientWallet.getId());
        transaction.setAmount(amount.negate()); // Lưu số âm
        transaction.setType("ESCROW_LOCK");
        transaction.setDescription("Tạm giữ tiền cho dự án ID: " + projectId);
        transactionRepository.save(transaction);

        // 5. Tạo bản ghi Escrow
        Escrow escrow = new Escrow();
        escrow.setClientId(clientId);
        escrow.setProjectId(projectId);
        escrow.setAmount(amount);
        escrow.setStatus("FUNDED"); // Đã nạp tiền ký quỹ thành công
        
        return escrowRepository.save(escrow);
    }
    
    // (Giữ nguyên hàm releaseFunds cũ ở dưới...)
}