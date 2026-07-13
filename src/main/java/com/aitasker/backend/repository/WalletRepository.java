package com.aitasker.backend.repository;

import com.aitasker.backend.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    // Quan trong: WalletController / WalletService dung ham nay de tim vi cua user
    Optional<Wallet> findByUserId(Long userId);
}
