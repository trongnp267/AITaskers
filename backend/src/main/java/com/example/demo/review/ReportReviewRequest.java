package com.example.demo.review;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReportReviewRequest(
        @NotBlank @Size(max = 1000) String reason
) {
}
