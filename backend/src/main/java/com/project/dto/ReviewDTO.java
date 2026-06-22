package com.project.dto;

import java.time.LocalDateTime;

public record ReviewDTO(
        Long id,
        String paymentId,
        String jobId,
        Long clientId,
        Long expertId,
        Integer score,
        String comment,
        LocalDateTime createdAt
) {
}
