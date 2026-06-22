package com.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.UUID;

/** Account entity. Class name is retained so the existing notification module stays compatible. */
@Entity @Table(name = "accounts", uniqueConstraints =
        @UniqueConstraint(name="uq_accounts_email", columnNames="email"))
public class Account {
    @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(name="account_id") private UUID id;
    @Email @Column(nullable=false, length=255) private String email;
    @Column(name="password_hash", length=255) private String passwordHash;
    @Column(name="full_name", nullable=false, length=100) private String name;
    @Column(name="auth_provider", nullable=false, length=20) private String authProvider = "LOCAL";
    @Column(name="google_id", length=255) private String googleId;
    @Column(name="account_status", nullable=false, length=20) private String accountStatus = "PENDING_ROLE";
    @Column(name="user_role", nullable=false, length=20) private String userRole;
    private Boolean subscribed = true;
    @Column(nullable=false, updatable=false) private LocalDateTime createdAt;
    @Column(nullable=false) private LocalDateTime updatedAt;
    @PrePersist void create(){ createdAt=updatedAt=LocalDateTime.now(); }
    @PreUpdate void update(){ updatedAt=LocalDateTime.now(); }
    public UUID getId(){return id;} public void setId(UUID v){id=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public Boolean getSubscribed(){return subscribed;} public void setSubscribed(Boolean v){subscribed=v==null?true:v;}
    public String getUserRole(){return userRole;} public void setUserRole(String v){userRole=v;}
    public void setPasswordHash(String v){passwordHash=v;} public void setAuthProvider(String v){authProvider=v;}
    public void setGoogleId(String v){googleId=v;} public void setAccountStatus(String v){accountStatus=v;}
}
