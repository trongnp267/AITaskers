package com.example.demo.review;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ReviewResponse createReview(
            Principal principal,
            @Valid @RequestBody CreateReviewRequest request
    ) {
        return reviewService.createReview(principal.getName(), request);
    }

    @PutMapping("/{id}")
    public ReviewResponse updateReview(
            Principal principal,
            @PathVariable Long id,
            @Valid @RequestBody UpdateReviewRequest request
    ) {
        return reviewService.updateReview(principal.getName(), id, request);
    }

    @GetMapping("/expert/{expertId}")
    public List<ReviewResponse> getExpertReviews(@PathVariable Long expertId) {
        return reviewService.getExpertReviews(expertId);
    }

    @GetMapping("/expert/{expertId}/summary")
    public ReviewSummaryResponse getExpertSummary(@PathVariable Long expertId) {
        return reviewService.getExpertSummary(expertId);
    }

    @GetMapping("/job/{jobId}")
    public List<ReviewResponse> getJobReviews(@PathVariable Long jobId) {
        return reviewService.getJobReviews(jobId);
    }

    @PostMapping("/{id}/report")
    public ReviewResponse reportReview(
            Principal principal,
            @PathVariable Long id,
            @Valid @RequestBody ReportReviewRequest request
    ) {
        return reviewService.reportReview(principal.getName(), id, request);
    }

    @GetMapping("/admin/reported")
    public List<ReviewResponse> getReportedReviews() {
        return reviewService.getReportedReviews();
    }

    @PutMapping("/admin/{id}/moderate")
    public ReviewResponse moderateReview(
            @PathVariable Long id,
            @Valid @RequestBody ModerateReviewRequest request
    ) {
        return reviewService.moderateReview(id, request);
    }

    @PostMapping("/admin/{id}/keep")
    public ResponseEntity<ReviewResponse> keepReview(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.moderateReview(
                id,
                new ModerateReviewRequest(ReviewStatus.ACTIVE, "Review kept by Admin")
        ));
    }
}
