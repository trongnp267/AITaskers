package com.aitasker.backend.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "notifications")
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(nullable = false)
    private Long recipientUserId;

    @Column(length = 1000)
    private String message;

    private String type;

    @Column(name = "is_read", nullable = false)
    private boolean read = false;

    private LocalDateTime createdAt = LocalDateTime.now();
}
