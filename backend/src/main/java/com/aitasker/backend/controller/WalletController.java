package com.aitasker.backend.controller;

import com.aitasker.backend.entity.Wallet;
import com.aitasker.backend.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletRepository walletRepository;

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestParam Long walletId, @RequestParam BigDecimal amount) {
        try {
            Wallet wallet = walletRepository.findById(walletId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy ví với ID: " + walletId));
            
            // Cầm số dư cũ cộng thêm tiền nạp mới (Giả sử biến số dư của bạn tên là balance)
            wallet.setBalance(wallet.getBalance().add(amount)); 
            walletRepository.save(wallet); // Lưu xuống Database
            
            return ResponseEntity.ok("Nạp thành công " + amount + " vào ví ID: " + walletId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi nạp tiền: " + e.getMessage());
        }
    }
}