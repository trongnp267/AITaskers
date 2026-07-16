package com.aitasker.backend.repository;

import com.aitasker.backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByExpertId(Long expertId);

    List<Review> findByJobId(Long jobId);
}
