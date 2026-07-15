package com.aitasker.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "milestones")
@Data
public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "milestone_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Job project;

    private String title;
    private String description;
    private BigDecimal amount;
    private String status;
    private LocalDateTime dueDate;

    public Long getProjectId() {
        return project != null ? project.getJobId() : null;
    }
}
