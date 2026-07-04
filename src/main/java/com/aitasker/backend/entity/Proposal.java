package com.aitasker.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Proposal")
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proposal_id")
    private Long proposalId;

    @ManyToOne
    @JoinColumn(name = "expert_id", nullable = false)
    private ExpertProfile expert;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(name = "cover_letter", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String coverLetter;

    @Column(name = "proposal_price", nullable = false)
    private BigDecimal proposalPrice;

    @Column(name = "estimated_days", nullable = false)
    private Integer estimatedDays;

    @Column(name = "proposal_status", nullable = false)
    private String proposalStatus;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

    public Proposal() {
    }

    public Long getProposalId() {
        return proposalId;
    }

    public void setProposalId(Long proposalId) {
        this.proposalId = proposalId;
    }

    public ExpertProfile getExpert() {
        return expert;
    }

    public void setExpert(ExpertProfile expert) {
        this.expert = expert;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public BigDecimal getProposalPrice() {
        return proposalPrice;
    }

    public void setProposalPrice(BigDecimal proposalPrice) {
        this.proposalPrice = proposalPrice;
    }

    public Integer getEstimatedDays() {
        return estimatedDays;
    }

    public void setEstimatedDays(Integer estimatedDays) {
        this.estimatedDays = estimatedDays;
    }

    public String getProposalStatus() {
        return proposalStatus;
    }

    public void setProposalStatus(String proposalStatus) {
        this.proposalStatus = proposalStatus;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}