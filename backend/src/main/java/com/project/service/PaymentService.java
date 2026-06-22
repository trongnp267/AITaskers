package com.project.service;

import com.project.dto.PaymentDTO;
import com.project.model.Payment;
import com.project.model.PaymentStatus;
import com.project.repository.IPaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final IPaymentRepository paymentRepository;

    public PaymentService(IPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public Payment save(PaymentDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Payment data is required");
        }

        String paymentId = requireText(dto.getPaymentId(), "Payment id is required");
        PaymentStatus status = PaymentStatus.from(dto.getStatus());
        Payment payment = paymentRepository.findById(paymentId)
                .orElseGet(() -> new Payment(paymentId, status));
        payment.setStatus(status);
        return paymentRepository.save(payment);
    }

    public Payment findById(String paymentId) {
        String id = requireText(paymentId, "Payment id is required");
        return paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + id));
    }

    private String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
