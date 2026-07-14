package com.example.demo.dto;

import java.math.BigDecimal;

public class ProposalRequest {

    private Long expertId;
    private Long jobId;
    private String coverLetter;
    private BigDecimal proposalPrice;
    private Integer estimatedDays;

    public Long getExpertId() {
        return expertId;
    }

    public void setExpertId(Long expertId) {
        this.expertId = expertId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
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
}