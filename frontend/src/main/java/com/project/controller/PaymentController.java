package com.project.controller;

import com.project.dto.PaymentDTO;
import com.project.model.Payment;
import com.project.service.IPaymentService;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    private final IPaymentService paymentService;

    public PaymentController(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payments")
    public Map<String, Object> savePayment(@RequestBody PaymentDTO paymentDTO) {
        Payment payment = paymentService.savePayment(paymentDTO);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("message", "Da cap nhat trang thai payment.");
        response.put("data", payment.toMap());
        return response;
    }

    @GetMapping("/payments")
    public Map<String, Object> getPayment(@RequestParam String paymentId) {
        Payment payment = paymentService.findById(paymentId);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("data", payment.toMap());
        return response;
    }
}
