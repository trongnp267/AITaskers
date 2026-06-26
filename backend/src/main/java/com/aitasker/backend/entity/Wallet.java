package com.aitasker.backend.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Data
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proposal_id")
private Long proposalId;
    private Long id;
    private Long userId;
    private BigDecimal balance;
}