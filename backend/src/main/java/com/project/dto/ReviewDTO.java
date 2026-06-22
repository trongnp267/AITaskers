package com.project.dto;
import com.project.model.ReviewStatus; import java.time.LocalDateTime; import java.util.UUID;
public record ReviewDTO(UUID reviewId, UUID projectId, UUID paymentId, UUID clientProfileId,
 UUID expertProfileId, Integer ratingStar, String reviewComment, Integer deliveryQuality,
 Integer communicationQuality, Integer deadlineSatisfaction, Boolean wouldHireAgain,
 ReviewStatus reviewStatus, LocalDateTime createdAt) {}
