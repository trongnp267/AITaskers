package com.aitasker.backend.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proposal_id")
private Long proposalId;
    private Long id;
    private Long walletId;
    private BigDecimal amount;
    private String type;
    private String description;
}