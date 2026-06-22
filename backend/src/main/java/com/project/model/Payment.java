package com.project.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milestone_id")
    private ProjectMilestone milestone;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_profile_id", nullable = false)
    private ClientProfile clientProfile;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "expert_profile_id", nullable = false)
    private ExpertProfile expertProfile;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;
    @Column(precision = 12, scale = 2)
    private BigDecimal platformFee;
    @Column(length = 50)
    private String paymentMethod;
    @Enumerated(EnumType.STRING) @Column(name = "payment_status", nullable = false, length = 20)
    private PaymentStatus status = PaymentStatus.HELD;
    private LocalDateTime releasedAt;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Payment() {}

    public Payment(Project project, ProjectMilestone milestone, ClientProfile clientProfile,
            ExpertProfile expertProfile, BigDecimal amount, PaymentStatus status) {
        this.project = project; this.milestone = milestone; this.clientProfile = clientProfile;
        this.expertProfile = expertProfile; this.amount = amount; this.status = status;
    }

    @PrePersist void prePersist() { if (createdAt == null) createdAt = LocalDateTime.now(); }
    public UUID getId() { return id; }
    public Project getProject() { return project; }
    public ProjectMilestone getMilestone() { return milestone; }
    public ClientProfile getClientProfile() { return clientProfile; }
    public ExpertProfile getExpertProfile() { return expertProfile; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getPlatformFee() { return platformFee; }
    public String getPaymentMethod() { return paymentMethod; }
    public PaymentStatus getStatus() { return status; }
    public LocalDateTime getReleasedAt() { return releasedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setStatus(PaymentStatus status) { this.status = status; if (status == PaymentStatus.RELEASED && releasedAt == null) releasedAt = LocalDateTime.now(); }
    public void setPlatformFee(BigDecimal value) { platformFee = value; }
    public void setPaymentMethod(String value) { paymentMethod = value; }
}
