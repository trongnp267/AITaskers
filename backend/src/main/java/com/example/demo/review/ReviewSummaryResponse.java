package com.example.demo.review;

public record ReviewSummaryResponse(
        Long userId,
        double averageRating,
        long totalReviews
) {
}
