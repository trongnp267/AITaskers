package com.project.dto;

public class RatingDTO {
    private String paymentId;
    private String jobId;
    private String userId;
    private String role;
    private String target;
    private String expertId;
    private Integer score;
    private String comment;

    public RatingDTO() {
    }

    public RatingDTO(String paymentId, String jobId, String userId, String role, String target, String expertId, Integer score, String comment) {
        this.paymentId = paymentId;
        this.jobId = jobId;
        this.userId = userId;
        this.role = role;
        this.target = target;
        this.expertId = expertId;
        this.score = score;
        this.comment = comment;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getExpertId() {
        return expertId;
    }

    public void setExpertId(String expertId) {
        this.expertId = expertId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
