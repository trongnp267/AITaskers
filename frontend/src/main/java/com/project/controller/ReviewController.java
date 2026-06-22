package com.project.controller;

import com.project.dto.ReviewRequest;
import com.project.entity.Review;
import com.project.service.ReviewService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createReview(@RequestBody ReviewRequest request) {
        Review review = reviewService.createReview(request);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("message", "Review da duoc ghi nhan.");
        response.put("data", review.toMap());
        return response;
    }

    @GetMapping("/reviews/expert/{expertId}")
    public Map<String, Object> getExpertReviews(@PathVariable String expertId) {
        List<Review> reviews = reviewService.findByExpertId(expertId);
        List<Map<String, Object>> data = new ArrayList<>();

        for (Review review : reviews) {
            data.add(review.toMap());
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("expertId", expertId);
        response.put("total", data.size());
        response.put("data", data);
        return response;
    }
}
