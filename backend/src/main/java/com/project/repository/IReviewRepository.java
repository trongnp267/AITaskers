package com.project.repository;
import com.project.model.Review; import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface IReviewRepository extends JpaRepository<Review, UUID> {
 boolean existsByPaymentId(UUID paymentId);
 List<Review> findByExpertProfileIdOrderByCreatedAtDesc(UUID expertProfileId);
}
