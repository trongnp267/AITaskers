package com.project.model;
import jakarta.persistence.*; import java.math.BigDecimal; import java.time.LocalDateTime; import java.util.UUID;
@Entity @Table(name="job_postings") public class JobPosting {
 @Id @GeneratedValue(strategy=GenerationType.UUID) @Column(name="job_id") private UUID id;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="client_profile_id",nullable=false) private ClientProfile clientProfile;
 @Column(nullable=false) private String jobTitle; @Column(nullable=false,columnDefinition="nvarchar(max)") private String description;
 @Column(nullable=false,columnDefinition="nvarchar(max)") private String requiredSkills; @Column(precision=12,scale=2) private BigDecimal budgetMin;
 @Column(precision=12,scale=2) private BigDecimal budgetMax; private String projectType; @Column(nullable=false,length=20) private String jobStatus="POSTED";
 @Column(nullable=false,updatable=false) private LocalDateTime createdAt; @Column(nullable=false) private LocalDateTime updatedAt;
 @PrePersist void create(){createdAt=updatedAt=LocalDateTime.now();} @PreUpdate void update(){updatedAt=LocalDateTime.now();} public UUID getId(){return id;}
}
