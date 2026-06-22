package com.project.model;

public enum PaymentStatus {
    HELD, RELEASED, REFUNDED, FAILED;
    public static PaymentStatus from(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Payment status is required");
        try { return valueOf(value.trim().toUpperCase()); }
        catch (IllegalArgumentException e) { throw new IllegalArgumentException("Payment status must be held, released, refunded or failed"); }
    }
}
