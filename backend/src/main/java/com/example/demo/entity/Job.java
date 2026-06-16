package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long jobId;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @JsonIgnore
    private ClientProfile client;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "position_requirement")
    private String positionRequirement;

    @Column(name = "required_technologies")
    private String requiredTechnologies;

    @Column(name = "min_experience_years")
    private Integer minExperienceYears;

    @Column(name = "budget_min", nullable = false)
    private BigDecimal budgetMin;

    @Column(name = "budget_max", nullable = false)
    private BigDecimal budgetMax;

    @Column(name = "job_status", nullable = false)
    private String jobStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deadline")
    private LocalDate deadline;

    public Job() {
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public ClientProfile getClient() {
        return client;
    }

    public void setClient(ClientProfile client) {
        this.client = client;
    }

    public Long getClientId() {
        return client != null ? client.getClientId() : null;
    }

    public String getClientCompanyName() {
        return client != null ? client.getCompanyName() : null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPositionRequirement() {
        return positionRequirement;
    }

    public void setPositionRequirement(String positionRequirement) {
        this.positionRequirement = positionRequirement;
    }

    public String getRequiredTechnologies() {
        return requiredTechnologies;
    }

    public void setRequiredTechnologies(String requiredTechnologies) {
        this.requiredTechnologies = requiredTechnologies;
    }

    public Integer getMinExperienceYears() {
        return minExperienceYears;
    }

    public void setMinExperienceYears(Integer minExperienceYears) {
        this.minExperienceYears = minExperienceYears;
    }

    public BigDecimal getBudgetMin() {
        return budgetMin;
    }

    public void setBudgetMin(BigDecimal budgetMin) {
        this.budgetMin = budgetMin;
    }

    public BigDecimal getBudgetMax() {
        return budgetMax;
    }

    public void setBudgetMax(BigDecimal budgetMax) {
        this.budgetMax = budgetMax;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}