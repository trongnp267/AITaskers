package com.project.dto;

import jakarta.validation.constraints.NotBlank;

public class PaymentDTO {

    @NotBlank
    private String paymentId;

    @NotBlank
    private String status;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
