package com.project.dto;

import com.project.model.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class NotificationDTO {

    private Long id;

    @NotNull
    private Long userId;

    @NotBlank
    private String title;

    @NotBlank
    private String message;

    private NotificationType type = NotificationType.SYSTEM_ANNOUNCEMENT;
    private String referenceType;
    private Long referenceId;
    private Boolean sent;
    private Boolean read;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        if (userId != null && userId <= 0) {
            throw new IllegalArgumentException("User id must be greater than 0");
        }
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title != null && title.isBlank()) {
            throw new IllegalArgumentException("Title must not be blank");
        }
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (message != null && message.isBlank()) {
            throw new IllegalArgumentException("Message must not be blank");
        }
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type == null ? NotificationType.SYSTEM_ANNOUNCEMENT : type;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        if (referenceType != null && referenceType.isBlank()) {
            throw new IllegalArgumentException("Reference type must not be blank");
        }
        this.referenceType = referenceType;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        if (referenceId != null && referenceId <= 0) {
            throw new IllegalArgumentException("Reference id must be greater than 0");
        }
        this.referenceId = referenceId;
    }

    public Boolean getSent() {
        return sent;
    }

    public void setSent(Boolean sent) {
        this.sent = sent == null ? false : sent;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read == null ? false : read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }
}
