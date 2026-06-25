package com.aitasker.backend.entity;

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
    private Long id;

    // Các cột dữ liệu dự đoán
    private Long jobId;
    private String title;
    private String description;
    private BigDecimal amount;
    private String status;
    private LocalDateTime dueDate;
}