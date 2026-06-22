package com.project.service;

import com.project.dto.RatingDTO;
import com.project.model.Rating;
import java.util.List;

public interface IRatingService {
    Rating createRating(RatingDTO ratingDTO);

    List<Rating> findRatings(String paymentId);
}
