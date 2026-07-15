package com.aitasker.backend.repository;

import com.aitasker.backend.entity.Escrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EscrowRepository extends JpaRepository<Escrow, Long> {

    Optional<Escrow> findByJobJobId(Long jobId);
}
