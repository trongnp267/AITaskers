package com.project.repository;

import com.project.model.Payment;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepository implements IPaymentRepository {
    private final Map<String, Payment> payments = new ConcurrentHashMap<>();

    @Override
    public Payment findById(String paymentId) {
        return payments.get(paymentId);
    }

    @Override
    public void save(Payment payment) {
        payments.put(payment.getPaymentId(), payment);
    }
}
