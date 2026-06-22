package com.project.service;

import com.project.dto.PaymentDTO;
import com.project.model.Payment;

public interface IPaymentService {
    Payment savePayment(PaymentDTO paymentDTO);

    Payment findById(String paymentId);
}
