package com.project.model;
import jakarta.persistence.*; import java.math.BigDecimal; import java.time.LocalDate; import java.time.LocalDateTime; import java.util.UUID;
@Entity @Table(name="projects") public class Project {
 @Id @GeneratedValue(strategy=GenerationType.UUID) @Column(name="project_id") private UUID id;
 @OneToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="job_id",nullable=false,unique=true) private JobPosting job;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="client_profile_id",nullable=false) private ClientProfile clientProfile;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="expert_profile_id",nullable=false) private ExpertProfile expertProfile;
 @Column(nullable=false) private String projectTitle; @Column(nullable=false,precision=12,scale=2) private BigDecimal agreedBudget;
 @Column(nullable=false,length=20) private String projectStatus="ACTIVE"; private LocalDate startDate; private LocalDate endDate;
 @Column(nullable=false,updatable=false) private LocalDateTime createdAt; @PrePersist void create(){createdAt=LocalDateTime.now();}
 public UUID getId(){return id;} public ClientProfile getClientProfile(){return clientProfile;} public ExpertProfile getExpertProfile(){return expertProfile;}
}
