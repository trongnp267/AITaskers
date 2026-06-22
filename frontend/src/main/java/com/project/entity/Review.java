package com.project.entity;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class Review {
    private final String id;
    private final String paymentId;
    private final String jobId;
    private final String clientId;
    private final String expertId;
    private final int score;
    private final String comment;
    private final String createdAt;

    public Review(
        String paymentId,
        String jobId,
        String clientId,
        String expertId,
        int score,
        String comment
    ) {
        this.id = UUID.randomUUID().toString();
        this.paymentId = paymentId;
        this.jobId = jobId;
        this.clientId = clientId;
        this.expertId = expertId;
        this.score = score;
        this.comment = comment == null ? "" : comment.trim();
        this.createdAt = Instant.now().toString();
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getExpertId() {
        return expertId;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("paymentId", paymentId);
        map.put("jobId", jobId);
        map.put("clientId", clientId);
        map.put("expertId", expertId);
        map.put("score", score);
        map.put("comment", comment);
        map.put("createdAt", createdAt);
        return map;
    }
}
