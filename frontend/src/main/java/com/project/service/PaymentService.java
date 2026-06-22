package com.project.service;

import com.project.dto.PaymentDTO;
import com.project.exception.PaymentException;
import com.project.model.Payment;
import com.project.model.PaymentStatus;
import com.project.repository.IPaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService implements IPaymentService {
    private final IPaymentRepository paymentRepository;

    public PaymentService(IPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment savePayment(PaymentDTO paymentDTO) {
        if (paymentDTO == null || isBlank(paymentDTO.getPaymentId()) || isBlank(paymentDTO.getStatus())) {
            throw new PaymentException(400, "paymentId va status la bat buoc.");
        }

        if (!isValidStatus(paymentDTO.getStatus())) {
            throw new PaymentException(400, "status chi duoc la unpaid, pending hoac paid.");
        }

        Payment payment = paymentRepository.findById(paymentDTO.getPaymentId());

        if (payment == null) {
            payment = new Payment(paymentDTO.getPaymentId(), paymentDTO.getStatus());
        } else {
            payment.setStatus(paymentDTO.getStatus());
        }

        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public Payment findById(String paymentId) {
        if (isBlank(paymentId)) {
            throw new PaymentException(400, "paymentId la bat buoc.");
        }

        Payment payment = paymentRepository.findById(paymentId);

        if (payment == null) {
            throw new PaymentException(404, "Khong tim thay payment.");
        }

        return payment;
    }

    private boolean isValidStatus(String status) {
        return PaymentStatus.UNPAID.equals(status)
            || PaymentStatus.PENDING.equals(status)
            || PaymentStatus.PAID.equals(status);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
