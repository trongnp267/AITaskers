package com.project.model;

public enum PaymentStatus {
    UNPAID,
    PENDING,
    PAID;

    public static PaymentStatus from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Payment status is required");
        }

        try {
            return PaymentStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Payment status must be unpaid, pending or paid");
        }
    }
}
