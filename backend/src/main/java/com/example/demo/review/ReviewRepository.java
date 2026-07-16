package com.example.demo.review;

import com.example.demo.entity.Job;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByJobAndReviewerAndRevieweeAndDirection(
            Job job,
            User reviewer,
            User reviewee,
            ReviewDirection direction
    );

    Optional<Review> findByIdAndReviewer(Long id, User reviewer);

    List<Review> findByRevieweeIdAndStatusOrderByCreatedAtDesc(Long revieweeId, ReviewStatus status);

    List<Review> findByJobJobIdAndStatusOrderByCreatedAtDesc(Long jobId, ReviewStatus status);

    List<Review> findByStatusOrderByUpdatedAtDesc(ReviewStatus status);

    @Query("""
            select coalesce(avg(r.rating), 0)
            from Review r
            where r.reviewee.id = :revieweeId and r.status = :status
            """)
    Double getAverageRating(@Param("revieweeId") Long revieweeId, @Param("status") ReviewStatus status);

    long countByRevieweeIdAndStatus(Long revieweeId, ReviewStatus status);
}
