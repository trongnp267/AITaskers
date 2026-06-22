package com.project.service;

import com.project.dto.ReviewDTO;
import com.project.dto.ReviewRequest;
import com.project.event.ReviewCreatedEvent;
import com.project.exception.ReviewException;
import com.project.model.Payment;
import com.project.model.PaymentStatus;
import com.project.model.Review;
import com.project.repository.IPaymentRepository;
import com.project.repository.IReviewRepository;
import com.project.repository.IUserRepository;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

    private final IPaymentRepository paymentRepository;
    private final IReviewRepository reviewRepository;
    private final IUserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ReviewService(IPaymentRepository paymentRepository, IReviewRepository reviewRepository,
            IUserRepository userRepository, ApplicationEventPublisher eventPublisher) {
        this.paymentRepository = paymentRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public ReviewDTO create(ReviewRequest request) {
        validate(request);

        String paymentId = request.getPaymentId().trim();
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ReviewException(HttpStatus.FORBIDDEN,
                        "Chua thanh toan nen khong the review."));

        if (payment.getStatus() == PaymentStatus.UNPAID) {
            throw new ReviewException(HttpStatus.FORBIDDEN, "Chua thanh toan nen khong the review.");
        }
        if (payment.getStatus() == PaymentStatus.PENDING) {
            throw new ReviewException(HttpStatus.FORBIDDEN, "Thanh toan dang pending nen chua the review.");
        }
        if (payment.getStatus() != PaymentStatus.PAID) {
            throw new ReviewException(HttpStatus.FORBIDDEN,
                    "Chi duoc review sau khi thanh toan thanh cong.");
        }

        requireUser(request.getClientId(), "Client");
        requireUser(request.getExpertId(), "Expert");

        if (reviewRepository.existsByPaymentIdAndClientIdAndExpertId(
                paymentId, request.getClientId(), request.getExpertId())) {
            throw new ReviewException(HttpStatus.CONFLICT,
                    "Client da review expert cho payment nay roi.");
        }

        Review review = new Review(
                paymentId,
                normalize(request.getJobId()),
                request.getClientId(),
                request.getExpertId(),
                request.getScore(),
                normalize(request.getComment())
        );

        try {
            Review saved = reviewRepository.saveAndFlush(review);
            eventPublisher.publishEvent(new ReviewCreatedEvent(
                    saved.getId(), saved.getClientId(), saved.getExpertId(), saved.getScore()));
            return toDTO(saved);
        } catch (DataIntegrityViolationException exception) {
            throw new ReviewException(HttpStatus.CONFLICT,
                    "Client da review expert cho payment nay roi.");
        }
    }

    @Transactional(readOnly = true)
    public List<ReviewDTO> findByExpertId(Long expertId) {
        if (expertId == null || expertId <= 0) {
            throw new ReviewException(HttpStatus.BAD_REQUEST, "Expert id must be greater than 0");
        }

        return reviewRepository.findByExpertIdOrderByCreatedAtDesc(expertId).stream()
                .map(this::toDTO)
                .toList();
    }

    private void validate(ReviewRequest request) {
        if (request == null) {
            throw new ReviewException(HttpStatus.BAD_REQUEST, "Body khong duoc de trong.");
        }
        if (request.getPaymentId() == null || request.getPaymentId().isBlank()) {
            throw new ReviewException(HttpStatus.BAD_REQUEST, "Payment id la bat buoc.");
        }
        if (request.getClientId() == null || request.getClientId() <= 0
                || request.getExpertId() == null || request.getExpertId() <= 0) {
            throw new ReviewException(HttpStatus.BAD_REQUEST,
                    "Client id va expert id phai lon hon 0.");
        }
        if (request.getClientId().equals(request.getExpertId())) {
            throw new ReviewException(HttpStatus.BAD_REQUEST,
                    "Client khong the tu review chinh minh.");
        }
        if (request.getScore() == null || request.getScore() < 1 || request.getScore() > 5) {
            throw new ReviewException(HttpStatus.BAD_REQUEST, "Score phai la so nguyen tu 1 den 5.");
        }
    }

    private void requireUser(Long userId, String label) {
        if (!userRepository.existsById(userId)) {
            throw new ReviewException(HttpStatus.NOT_FOUND, label + " not found: " + userId);
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private ReviewDTO toDTO(Review review) {
        return new ReviewDTO(
                review.getId(), review.getPaymentId(), review.getJobId(), review.getClientId(),
                review.getExpertId(), review.getScore(), review.getComment(), review.getCreatedAt());
    }
}
