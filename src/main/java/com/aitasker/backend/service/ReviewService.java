package com.aitasker.backend.service;

import com.aitasker.backend.dto.ReviewRequest;
import com.aitasker.backend.entity.ExpertProfile;
import com.aitasker.backend.entity.Review;
import com.aitasker.backend.exception.BadRequestException;
import com.aitasker.backend.exception.ResourceNotFoundException;
import com.aitasker.backend.repository.ExpertProfileRepository;
import com.aitasker.backend.repository.JobRepository;
import com.aitasker.backend.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ExpertProfileRepository expertProfileRepository;
    private final JobRepository jobRepository;
    private final NotificationService notificationService;

    public ReviewService(ReviewRepository reviewRepository,
                         ExpertProfileRepository expertProfileRepository,
                         JobRepository jobRepository,
                         NotificationService notificationService) {
        this.reviewRepository = reviewRepository;
        this.expertProfileRepository = expertProfileRepository;
        this.jobRepository = jobRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public Review createReview(ReviewRequest request) {
        validate(request);

        jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        ExpertProfile expert = expertProfileRepository.findById(request.getExpertId())
                .orElseThrow(() -> new ResourceNotFoundException("Expert not found"));

        Review review = new Review();
        review.setJobId(request.getJobId());
        review.setReviewerUserId(request.getReviewerUserId());
        review.setExpertId(request.getExpertId());
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        Review saved = reviewRepository.save(review);

        recomputeExpertRating(expert);

        notificationService.createNotification(
                expert.getUser().getId(),
                "NEW_REVIEW",
                "Ban vua nhan mot danh gia moi: " + request.getRating() + " sao.");

        return saved;
    }

    public List<Review> getReviewsForExpert(Long expertId) {
        return reviewRepository.findByExpertId(expertId);
    }

    private void recomputeExpertRating(ExpertProfile expert) {
        List<Review> reviews = reviewRepository.findByExpertId(expert.getExpertId());
        if (reviews.isEmpty()) {
            return;
        }
        double avg = reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
        expert.setRating(BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP));
        expertProfileRepository.save(expert);
    }

    private void validate(ReviewRequest request) {
        if (request.getJobId() == null) {
            throw new BadRequestException("Job ID is required");
        }
        if (request.getReviewerUserId() == null) {
            throw new BadRequestException("Reviewer user ID is required");
        }
        if (request.getExpertId() == null) {
            throw new BadRequestException("Expert ID is required");
        }
        if (request.getRating() == null || request.getRating() < 1 || request.getRating() > 5) {
            throw new BadRequestException("Rating must be between 1 and 5");
        }
    }
}
