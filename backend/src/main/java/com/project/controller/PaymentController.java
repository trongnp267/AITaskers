package com.project.controller;

import com.project.dto.PaymentDTO;
import com.project.model.Payment;
import com.project.service.PaymentService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping({"/api/project/payments", "/payments"})
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody PaymentDTO dto) {
        return ResponseEntity.ok(response(paymentService.save(dto)));
    }

    @GetMapping
    public ResponseEntity<?> findById(@RequestParam UUID paymentId) {
        return ResponseEntity.ok(response(paymentService.findById(paymentId)));
    }

    private Map<String, Object> response(Payment payment) {
        return Map.of(
                "paymentId", payment.getId(),
                "status", payment.getStatus().name().toLowerCase(),
                "createdAt", payment.getCreatedAt()
        );
    }
}
