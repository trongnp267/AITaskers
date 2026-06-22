package com.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(
        name = "project_reviews",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_project_reviews_payment_client_expert",
                columnNames = {"payment_id", "client_id", "expert_id"}
        )
)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_id", nullable = false, length = 100)
    private String paymentId;

    @Nationalized
    @Column(name = "job_id", length = 100)
    private String jobId;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "expert_id", nullable = false)
    private Long expertId;

    @Column(nullable = false)
    private Integer score;

    @Nationalized
    @Column(length = 2000)
    private String comment;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected Review() {
    }

    public Review(String paymentId, String jobId, Long clientId, Long expertId, Integer score, String comment) {
        this.paymentId = paymentId;
        this.jobId = jobId;
        this.clientId = clientId;
        this.expertId = expertId;
        this.score = score;
        this.comment = comment;
    }

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getJobId() {
        return jobId;
    }

    public Long getClientId() {
        return clientId;
    }

    public Long getExpertId() {
        return expertId;
    }

    public Integer getScore() {
        return score;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
