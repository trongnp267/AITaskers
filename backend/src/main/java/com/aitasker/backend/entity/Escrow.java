package com.aitasker.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "escrows")
@Data
public class Escrow {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    

    private Long id;

    // Các cột dữ liệu dự đoán (có thể phải thêm nếu Service gọi)
    private Long jobId;
    private Long proposalId;
    private BigDecimal amount;
    private String status;
    private Long projectId;
}