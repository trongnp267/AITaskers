package com.project.repository;

import com.project.model.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByPaymentIdAndClientIdAndExpertId(String paymentId, Long clientId, Long expertId);

    List<Review> findByExpertIdOrderByCreatedAtDesc(Long expertId);
}
