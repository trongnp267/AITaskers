package com.project.service;

import com.project.dto.ReviewRequest;
import com.project.entity.Review;
import com.project.exception.ReviewException;
import com.project.model.Payment;
import com.project.model.PaymentStatus;
import com.project.repository.IPaymentRepository;
import com.project.repository.ReviewRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private final IPaymentRepository paymentRepository;
    private final ReviewRepository reviewRepository;

    public ReviewService(IPaymentRepository paymentRepository, ReviewRepository reviewRepository) {
        this.paymentRepository = paymentRepository;
        this.reviewRepository = reviewRepository;
    }

    public Review createReview(ReviewRequest request) {
        if (request == null) {
            throw new ReviewException(400, "Body khong duoc de trong.");
        }

        if (
            isBlank(request.getPaymentId())
                || isBlank(request.getClientId())
                || isBlank(request.getExpertId())
        ) {
            throw new ReviewException(400, "paymentId, clientId va expertId la bat buoc.");
        }

        if (request.getScore() == null || request.getScore() < 1 || request.getScore() > 5) {
            throw new ReviewException(400, "score phai la so nguyen tu 1 den 5.");
        }

        Payment payment = paymentRepository.findById(request.getPaymentId());

        if (payment == null || PaymentStatus.UNPAID.equals(payment.getStatus())) {
            throw new ReviewException(403, "Chua thanh toan nen khong the review.");
        }

        if (PaymentStatus.PENDING.equals(payment.getStatus())) {
            throw new ReviewException(403, "Thanh toan dang pending nen chua the review.");
        }

        if (!PaymentStatus.PAID.equals(payment.getStatus())) {
            throw new ReviewException(403, "Chi duoc review sau khi thanh toan thanh cong.");
        }

        if (reviewRepository.exists(request.getPaymentId(), request.getClientId(), request.getExpertId())) {
            throw new ReviewException(409, "Client da review expert cho payment nay roi.");
        }

        Review review = new Review(
            request.getPaymentId().trim(),
            normalize(request.getJobId()),
            request.getClientId().trim(),
            request.getExpertId().trim(),
            request.getScore(),
            request.getComment()
        );

        reviewRepository.save(review);
        return review;
    }

    public List<Review> findByExpertId(String expertId) {
        if (isBlank(expertId)) {
            throw new ReviewException(400, "expertId la bat buoc.");
        }

        return reviewRepository.findByExpertId(expertId.trim());
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
