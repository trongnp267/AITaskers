package com.project.model;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class Rating {
    private final String id;
    private final String paymentId;
    private final String jobId;
    private final String userId;
    private final String role;
    private final String target;
    private final String expertId;
    private final int score;
    private final String comment;
    private final String createdAt;

    public Rating(String paymentId, String jobId, String userId, String role, String target, String expertId, int score, String comment) {
        this.id = UUID.randomUUID().toString();
        this.paymentId = paymentId;
        this.jobId = jobId;
        this.userId = userId;
        this.role = role;
        this.target = target;
        this.expertId = expertId;
        this.score = score;
        this.comment = comment;
        this.createdAt = Instant.now().toString();
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getUserId() {
        return userId;
    }

    public String getTarget() {
        return target;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("paymentId", paymentId);
        map.put("jobId", jobId);
        map.put("userId", userId);
        map.put("role", role);
        map.put("target", target);
        map.put("expertId", expertId);
        map.put("score", score);
        map.put("comment", comment);
        map.put("createdAt", createdAt);
        return map;
    }
}
