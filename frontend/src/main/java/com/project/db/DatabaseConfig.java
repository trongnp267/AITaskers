package com.project.db;

import com.project.repository.IPaymentRepository;
import com.project.repository.IRatingRepository;
import com.project.repository.PaymentRepository;
import com.project.repository.RatingRepository;

public class DatabaseConfig {
    private final IPaymentRepository paymentRepository;
    private final IRatingRepository ratingRepository;

    public DatabaseConfig() {
        this.paymentRepository = new PaymentRepository();
        this.ratingRepository = new RatingRepository();
    }

    public IPaymentRepository getPaymentRepository() {
        return paymentRepository;
    }

    public IRatingRepository getRatingRepository() {
        return ratingRepository;
    }
}
