package com.example.demo.review;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Long jobId,
        String jobTitle,
        Long reviewerId,
        String reviewerUsername,
        Long revieweeId,
        String revieweeUsername,
        ReviewDirection direction,
        Integer rating,
        String comment,
        ReviewStatus status,
        String reportReason,
        String moderationNote,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getJob().getJobId(),
                review.getJob().getTitle(),
                review.getReviewer().getId(),
                review.getReviewer().getUsername(),
                review.getReviewee().getId(),
                review.getReviewee().getUsername(),
                review.getDirection(),
                review.getRating(),
                review.getComment(),
                review.getStatus(),
                review.getReportReason(),
                review.getModerationNote(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}
