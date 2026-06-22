package com.project.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "reviews", uniqueConstraints = @UniqueConstraint(name = "uq_reviews_payment", columnNames = "payment_id"))
@Check(constraints = "rating_star between 1 and 5")
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "review_id") private UUID id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    @OneToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "client_profile_id", nullable = false)
    private ClientProfile clientProfile;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "expert_profile_id", nullable = false)
    private ExpertProfile expertProfile;
    @Column(name = "rating_star", nullable = false) private Integer ratingStar;
    @Nationalized @Column(name = "review_comment", columnDefinition = "nvarchar(max)") private String reviewComment;
    private Integer deliveryQuality;
    private Integer communicationQuality;
    private Integer deadlineSatisfaction;
    private Boolean wouldHireAgain;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private ReviewStatus reviewStatus = ReviewStatus.PUBLISHED;
    @Column(nullable = false, updatable = false) private LocalDateTime createdAt;

    protected Review() {}
    public Review(Payment payment, Integer ratingStar, String comment) {
        this.payment = payment; this.project = payment.getProject(); this.clientProfile = payment.getClientProfile();
        this.expertProfile = payment.getExpertProfile(); this.ratingStar = ratingStar; this.reviewComment = comment;
    }
    @PrePersist void prePersist() { if (createdAt == null) createdAt = LocalDateTime.now(); }
    public UUID getId() { return id; }
    public Project getProject() { return project; }
    public Payment getPayment() { return payment; }
    public ClientProfile getClientProfile() { return clientProfile; }
    public ExpertProfile getExpertProfile() { return expertProfile; }
    public Integer getRatingStar() { return ratingStar; }
    public String getReviewComment() { return reviewComment; }
    public Integer getDeliveryQuality() { return deliveryQuality; }
    public Integer getCommunicationQuality() { return communicationQuality; }
    public Integer getDeadlineSatisfaction() { return deadlineSatisfaction; }
    public Boolean getWouldHireAgain() { return wouldHireAgain; }
    public ReviewStatus getReviewStatus() { return reviewStatus; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setDetails(Integer delivery, Integer communication, Integer deadline, Boolean hireAgain) {
        deliveryQuality=delivery; communicationQuality=communication; deadlineSatisfaction=deadline; wouldHireAgain=hireAgain;
    }
}
