package com.example.demo.review;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ModerateReviewRequest(
        @NotNull ReviewStatus status,
        @Size(max = 1000) String moderationNote
) {
}
