package com.project.model;
import jakarta.persistence.*; import java.math.BigDecimal; import java.time.LocalDate; import java.time.LocalDateTime; import java.util.UUID;
@Entity @Table(name="project_milestones") public class ProjectMilestone {
 @Id @GeneratedValue(strategy=GenerationType.UUID) @Column(name="milestone_id") private UUID id;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="project_id",nullable=false) private Project project;
 @Column(nullable=false) private String milestoneName; @Column(columnDefinition="nvarchar(max)") private String description;
 @Column(nullable=false,precision=12,scale=2) private BigDecimal amount; private LocalDate dueDate;
 @Column(nullable=false,length=20) private String milestoneStatus="PENDING"; @Column(nullable=false,updatable=false) private LocalDateTime createdAt;
 @PrePersist void create(){createdAt=LocalDateTime.now();} public UUID getId(){return id;} public Project getProject(){return project;}
}
