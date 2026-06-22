package com.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "project_payments")
public class Payment {

    @Id
    @Column(length = 100)
    private String id;

    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(nullable = false, length = 20)
    private PaymentStatus status = PaymentStatus.UNPAID;

    @Column
    private LocalDateTime updatedAt;

    protected Payment() {
    }

    public Payment(String id, PaymentStatus status) {
        this.id = id;
        this.status = status;
    }

    @PrePersist
    @PreUpdate
    void updateTimestamp() {
        updatedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
