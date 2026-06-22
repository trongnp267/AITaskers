package com.project.repository;

import com.project.entity.Review;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepository {
    private final List<Review> reviews = new ArrayList<>();

    public boolean exists(String paymentId, String clientId, String expertId) {
        synchronized (reviews) {
            for (Review review : reviews) {
                if (
                    review.getPaymentId().equals(paymentId)
                        && review.getClientId().equals(clientId)
                        && review.getExpertId().equals(expertId)
                ) {
                    return true;
                }
            }
        }

        return false;
    }

    public void save(Review review) {
        synchronized (reviews) {
            reviews.add(review);
        }
    }

    public List<Review> findByExpertId(String expertId) {
        List<Review> result = new ArrayList<>();

        synchronized (reviews) {
            for (Review review : reviews) {
                if (review.getExpertId().equals(expertId)) {
                    result.add(review);
                }
            }
        }

        return result;
    }
}
