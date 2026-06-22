package com.project.repository;

import com.project.model.Rating;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class RatingRepository implements IRatingRepository {
    private final List<Rating> ratings = new ArrayList<>();

    @Override
    public boolean existsByPaymentIdAndUserIdAndTarget(String paymentId, String userId, String target) {
        synchronized (ratings) {
            for (Rating rating : ratings) {
                if (
                    rating.getPaymentId().equals(paymentId)
                        && rating.getUserId().equals(userId)
                        && rating.getTarget().equals(target)
                ) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void save(Rating rating) {
        synchronized (ratings) {
            ratings.add(rating);
        }
    }

    @Override
    public List<Rating> findAll() {
        synchronized (ratings) {
            return new ArrayList<>(ratings);
        }
    }

    @Override
    public List<Rating> findByPaymentId(String paymentId) {
        List<Rating> result = new ArrayList<>();

        synchronized (ratings) {
            for (Rating rating : ratings) {
                if (rating.getPaymentId().equals(paymentId)) {
                    result.add(rating);
                }
            }
        }

        return result;
    }
}
