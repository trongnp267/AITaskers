package com.project.dto;

public class PaymentDTO {
    private String paymentId;
    private String status;

    public PaymentDTO() {
    }

    public PaymentDTO(String paymentId, String status) {
        this.paymentId = paymentId;
        this.status = status;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        if (status == null) {
            return "";
        }

        return status.trim().toLowerCase();
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
