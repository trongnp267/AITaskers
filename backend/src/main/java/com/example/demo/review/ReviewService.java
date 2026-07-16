package com.example.demo.review;

import com.example.demo.entity.ExpertProfile;
import com.example.demo.entity.Job;
import com.example.demo.entity.Proposal;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.notification.NotificationService;
import com.example.demo.notification.NotificationType;
import com.example.demo.repository.ExpertProfileRepository;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.ProposalRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class ReviewService {
    private static final Duration EDIT_WINDOW = Duration.ofDays(7);
    private static final Set<String> REVIEWABLE_JOB_STATUSES = Set.of("COMPLETED", "PAID");
    private static final Set<String> ACCEPTED_PROPOSAL_STATUSES = Set.of(
            "ACCEPTED",
            "APPROVED",
            "HIRED",
            "COMPLETED",
            "PAID"
    );

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ExpertProfileRepository expertProfileRepository;
    private final ProposalRepository proposalRepository;
    private final NotificationService notificationService;

    public ReviewService(
            ReviewRepository reviewRepository,
            UserRepository userRepository,
            JobRepository jobRepository,
            ExpertProfileRepository expertProfileRepository,
            ProposalRepository proposalRepository,
            NotificationService notificationService
    ) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.expertProfileRepository = expertProfileRepository;
        this.proposalRepository = proposalRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public ReviewResponse createReview(String username, CreateReviewRequest request) {
        User reviewer = getUser(username);
        User reviewee = userRepository.findById(request.revieweeId())
                .orElseThrow(() -> new ResourceNotFoundException("Reviewee not found"));
        Job job = getJob(request.jobId());

        validateNotSelfReview(reviewer, reviewee);
        validateReviewableJob(job);
        validateRelationship(reviewer, reviewee, job, request.direction());
        validateReviewIsUnique(job, reviewer, reviewee, request.direction());

        Review review = new Review();
        review.setJob(job);
        review.setReviewer(reviewer);
        review.setReviewee(reviewee);
        review.setDirection(request.direction());
        review.setRating(request.rating());
        review.setComment(request.comment().trim());

        Review savedReview = reviewRepository.save(review);
        sendReviewNotification(savedReview, false);
        return ReviewResponse.from(savedReview);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getExpertReviews(Long expertId) {
        ExpertProfile expert = expertProfileRepository.findById(expertId)
                .orElseThrow(() -> new ResourceNotFoundException("Expert not found"));
        return reviewRepository
                .findByRevieweeIdAndStatusOrderByCreatedAtDesc(expert.getUser().getId(), ReviewStatus.ACTIVE)
                .stream()
                .map(ReviewResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getJobReviews(Long jobId) {
        return reviewRepository.findByJobJobIdAndStatusOrderByCreatedAtDesc(jobId, ReviewStatus.ACTIVE)
                .stream()
                .map(ReviewResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReviewSummaryResponse getExpertSummary(Long expertId) {
        ExpertProfile expert = expertProfileRepository.findById(expertId)
                .orElseThrow(() -> new ResourceNotFoundException("Expert not found"));
        Long userId = expert.getUser().getId();
        return new ReviewSummaryResponse(
                userId,
                reviewRepository.getAverageRating(userId, ReviewStatus.ACTIVE),
                reviewRepository.countByRevieweeIdAndStatus(userId, ReviewStatus.ACTIVE)
        );
    }

    @Transactional
    public ReviewResponse updateReview(String username, Long reviewId, UpdateReviewRequest request) {
        User reviewer = getUser(username);
        Review review = reviewRepository.findByIdAndReviewer(reviewId, reviewer)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (review.getStatus() != ReviewStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only active reviews can be updated");
        }

        if (review.getCreatedAt().plus(EDIT_WINDOW).isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Review edit window has expired");
        }

        review.setRating(request.rating());
        review.setComment(request.comment().trim());
        Review savedReview = reviewRepository.save(review);
        sendReviewNotification(savedReview, true);
        return ReviewResponse.from(savedReview);
    }

    @Transactional
    public ReviewResponse reportReview(String username, Long reviewId, ReportReviewRequest request) {
        User reporter = getUser(username);
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (review.getReviewer().getId().equals(reporter.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot report your own review");
        }

        review.setStatus(ReviewStatus.REPORTED);
        review.setReportReason(request.reason().trim());
        return ReviewResponse.from(reviewRepository.save(review));
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReportedReviews() {
        return reviewRepository.findByStatusOrderByUpdatedAtDesc(ReviewStatus.REPORTED)
                .stream()
                .map(ReviewResponse::from)
                .toList();
    }

    @Transactional
    public ReviewResponse moderateReview(Long reviewId, ModerateReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        review.setStatus(request.status());
        review.setModerationNote(request.moderationNote());
        Review savedReview = reviewRepository.save(review);

        notificationService.createNotification(
                savedReview.getReviewee(),
                NotificationType.REVIEW_MODERATED,
                "Review has been moderated",
                "A review on your profile was processed by Admin.",
                "/reviews/" + savedReview.getId()
        );

        return ReviewResponse.from(savedReview);
    }

    private void validateRelationship(User reviewer, User reviewee, Job job, ReviewDirection direction) {
        if (direction == ReviewDirection.CLIENT_TO_EXPERT) {
            validateClientOwnsJob(reviewer, job);
            validateExpertWorkedOnJob(reviewee, job);
            return;
        }

        validateExpertWorkedOnJob(reviewer, job);
        if (!job.getClient().getUser().getId().equals(reviewee.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Expert can only review the job client");
        }
    }

    private void validateClientOwnsJob(User reviewer, Job job) {
        if (!job.getClient().getUser().getId().equals(reviewer.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the job client can review this expert");
        }
    }

    private void validateExpertWorkedOnJob(User expertUser, Job job) {
        Proposal proposal = proposalRepository.findByExpertIdAndJobJobId(expertUser.getId(), job.getJobId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "This expert is not related to the job"
                ));

        if (!ACCEPTED_PROPOSAL_STATUSES.contains(proposal.getProposalStatus().toUpperCase())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only accepted experts can be reviewed for this job"
            );
        }
    }

    private void validateReviewableJob(Job job) {
        if (!REVIEWABLE_JOB_STATUSES.contains(job.getJobStatus().toUpperCase())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Reviews are only allowed when the job is COMPLETED or PAID"
            );
        }
    }

    private void validateReviewIsUnique(Job job, User reviewer, User reviewee, ReviewDirection direction) {
        if (reviewRepository.existsByJobAndReviewerAndRevieweeAndDirection(job, reviewer, reviewee, direction)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This user has already reviewed this person for this job"
            );
        }
    }

    private void validateNotSelfReview(User reviewer, User reviewee) {
        if (reviewer.getId().equals(reviewee.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Users cannot review themselves");
        }
    }

    private void sendReviewNotification(Review review, boolean updated) {
        notificationService.createNotification(
                review.getReviewee(),
                updated ? NotificationType.REVIEW_UPDATED : NotificationType.REVIEW_RECEIVED,
                updated ? "Review updated" : "New review received",
                review.getReviewer().getUsername() + " rated you " + review.getRating() + " stars.",
                "/job/details/" + review.getJob().getJobId()
        );

        notificationService.createNotification(
                review.getReviewee(),
                NotificationType.RATING_RECEIVED,
                "New rating received",
                "Your rating was updated with a " + review.getRating() + "-star review.",
                "/job/details/" + review.getJob().getJobId()
        );
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Job getJob(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
    }
}
