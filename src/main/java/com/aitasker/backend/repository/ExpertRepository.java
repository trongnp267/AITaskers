package com.aitasker.backend.repository;

import com.aitasker.backend.entity.ExpertProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExpertRepository extends JpaRepository<ExpertProfile, Long> {
    // Lọc lấy tất cả expert để AI chấm điểm
    List<ExpertProfile> findAll();
}