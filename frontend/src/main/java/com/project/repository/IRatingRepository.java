package com.project.repository;

import com.project.model.Rating;
import java.util.List;

public interface IRatingRepository {
    boolean existsByPaymentIdAndUserIdAndTarget(String paymentId, String userId, String target);

    void save(Rating rating);

    List<Rating> findAll();

    List<Rating> findByPaymentId(String paymentId);
}
