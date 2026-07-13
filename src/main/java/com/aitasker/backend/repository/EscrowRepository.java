package com.aitasker.backend.repository;

import com.aitasker.backend.entity.Escrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EscrowRepository extends JpaRepository<Escrow, Long> {

    // TRUOC DAY: findByProjectId(Long projectId) -> Escrow KHONG CO field
    // "projectId" (chi co field "job" kieu Job), nen Spring Data JPA se nem
    // PropertyReferenceException va ung dung CRASH ngay khi khoi dong.
    // Escrow la quan he 1-1 voi Job nen dung findByJobJobId la dung.
    Optional<Escrow> findByJobJobId(Long jobId);
}
