package com.project.repository;

import com.project.model.Payment;

public interface IPaymentRepository {
    Payment findById(String paymentId);

    void save(Payment payment);
}
