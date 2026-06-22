package com.project.service;

import com.project.dto.RatingDTO;
import com.project.exception.RatingException;
import com.project.model.Payment;
import com.project.model.PaymentStatus;
import com.project.model.Rating;
import com.project.model.RatingTarget;
import com.project.model.Role;
import com.project.repository.IPaymentRepository;
import com.project.repository.IRatingRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RatingService implements IRatingService {
    private final IPaymentRepository paymentRepository;
    private final IRatingRepository ratingRepository;

    public RatingService(IPaymentRepository paymentRepository, IRatingRepository ratingRepository) {
        this.paymentRepository = paymentRepository;
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Rating createRating(RatingDTO ratingDTO) {
        if (ratingDTO == null) {
            throw new RatingException(400, "Body khong duoc de trong.");
        }

        if (
            isBlank(ratingDTO.getPaymentId())
                || isBlank(ratingDTO.getUserId())
                || isBlank(ratingDTO.getRole())
                || isBlank(ratingDTO.getTarget())
        ) {
            throw new RatingException(400, "paymentId, userId, role va target la bat buoc.");
        }

        if (!Role.CLIENT.equals(ratingDTO.getRole()) && !Role.EXPERT.equals(ratingDTO.getRole())) {
            throw new RatingException(400, "role chi duoc la client hoac expert.");
        }

        if (
            !RatingTarget.APP.equals(ratingDTO.getTarget())
                && !RatingTarget.EXPERT.equals(ratingDTO.getTarget())
                && !RatingTarget.GEMINI.equals(ratingDTO.getTarget())
        ) {
            throw new RatingException(400, "target chi duoc la app, expert hoac gemini.");
        }

        if (ratingDTO.getScore() == null || ratingDTO.getScore() < 1 || ratingDTO.getScore() > 5) {
            throw new RatingException(400, "score phai la so nguyen tu 1 den 5.");
        }

        Payment payment = paymentRepository.findById(ratingDTO.getPaymentId());

        if (payment == null || PaymentStatus.UNPAID.equals(payment.getStatus())) {
            throw new RatingException(403, "Chua thanh toan nen khong the danh gia.");
        }

        if (PaymentStatus.PENDING.equals(payment.getStatus())) {
            throw new RatingException(403, "Thanh toan dang pending nen chua the danh gia.");
        }

        if (!PaymentStatus.PAID.equals(payment.getStatus())) {
            throw new RatingException(403, "Chi duoc danh gia sau khi thanh toan thanh cong.");
        }

        if (
            Role.EXPERT.equals(ratingDTO.getRole())
                && !RatingTarget.APP.equals(ratingDTO.getTarget())
                && !RatingTarget.GEMINI.equals(ratingDTO.getTarget())
        ) {
            throw new RatingException(403, "Expert chi duoc danh gia ung dung hoac Gemini.");
        }

        if (
            Role.CLIENT.equals(ratingDTO.getRole())
                && RatingTarget.EXPERT.equals(ratingDTO.getTarget())
                && isBlank(ratingDTO.getExpertId())
        ) {
            throw new RatingException(400, "Client can expertId de danh gia expert.");
        }

        if (
            ratingRepository.existsByPaymentIdAndUserIdAndTarget(
                ratingDTO.getPaymentId(),
                ratingDTO.getUserId(),
                ratingDTO.getTarget()
            )
        ) {
            throw new RatingException(409, "Ban da danh gia muc nay cho payment nay roi.");
        }

        String expertId = RatingTarget.EXPERT.equals(ratingDTO.getTarget()) ? ratingDTO.getExpertId() : "";
        Rating rating = new Rating(
            ratingDTO.getPaymentId(),
            ratingDTO.getJobId(),
            ratingDTO.getUserId(),
            ratingDTO.getRole(),
            ratingDTO.getTarget(),
            expertId,
            ratingDTO.getScore(),
            ratingDTO.getComment()
        );

        ratingRepository.save(rating);
        return rating;
    }

    @Override
    public List<Rating> findRatings(String paymentId) {
        if (paymentId == null || paymentId.trim().isEmpty()) {
            return ratingRepository.findAll();
        }

        return ratingRepository.findByPaymentId(paymentId);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
