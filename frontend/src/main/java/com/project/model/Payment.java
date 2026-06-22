package com.project.model;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

public class Payment {
    private final String paymentId;
    private String status;
    private String updatedAt;

    public Payment(String paymentId, String status) {
        this.paymentId = paymentId;
        this.status = status;
        this.updatedAt = Instant.now().toString();
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = Instant.now().toString();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("paymentId", paymentId);
        map.put("status", status);
        map.put("updatedAt", updatedAt);
        return map;
    }
}
