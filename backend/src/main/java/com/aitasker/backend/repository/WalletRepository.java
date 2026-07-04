package com.aitasker.backend.repository;

import com.aitasker.backend.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    // Hàm này rất quan trọng để WalletService tìm ví của user (dòng 28)
    Optional<Wallet> findByUserId(Long userId);
}