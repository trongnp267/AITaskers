package com.aitasker.backend.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Proposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long jobId;
    private Long freelancerId;
}