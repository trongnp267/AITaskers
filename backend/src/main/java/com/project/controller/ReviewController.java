package com.project.controller;

import com.project.dto.ReviewDTO;
import com.project.dto.ReviewRequest;
import com.project.service.ReviewService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/project/reviews", "/reviews"})
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ReviewRequest request) {
        ReviewDTO review = reviewService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Review da duoc ghi nhan.",
                "data", review
        ));
    }

    @GetMapping("/expert/{expertId}")
    public ResponseEntity<?> findByExpert(@PathVariable UUID expertId) {
        List<ReviewDTO> reviews = reviewService.findByExpertId(expertId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "expertId", expertId,
                "total", reviews.size(),
                "data", reviews
        ));
    }
}
