package com.aitasker.backend.controller;

import com.aitasker.backend.dto.ReviewRequest;
import com.aitasker.backend.entity.Review;
import com.aitasker.backend.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody ReviewRequest request) {
        Review created = reviewService.createReview(request);
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "Danh gia thanh cong.",
            "data", created
        ));
    }

    @GetMapping("/expert/{expertId}")
    public ResponseEntity<List<Review>> getReviewsForExpert(@PathVariable Long expertId) {
        return ResponseEntity.ok(reviewService.getReviewsForExpert(expertId));
    }
}
