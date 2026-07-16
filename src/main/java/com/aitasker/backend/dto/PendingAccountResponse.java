package com.aitasker.backend.dto;

import com.aitasker.backend.entity.AccountStatus;

import java.time.LocalDateTime;

public class PendingAccountResponse {

    private Long id;
    private String username;
    private String role;
    private AccountStatus status;
    private LocalDateTime createdAt;

    public PendingAccountResponse() {
    }

    public PendingAccountResponse(Long id, String username, String role, AccountStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
