package com.project.model;
import jakarta.persistence.*; import java.time.LocalDateTime; import java.util.UUID;
@Entity @Table(name="client_profiles")
public class ClientProfile {
 @Id @GeneratedValue(strategy=GenerationType.UUID) @Column(name="client_profile_id") private UUID id;
 @OneToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="account_id",nullable=false,unique=true) private Account account;
 @Column(nullable=false) private String companyName; @Column(nullable=false,length=100) private String industry;
 private String companySize; @Column(columnDefinition="nvarchar(max)") private String aiNeeds;
 @Column(columnDefinition="nvarchar(max)") private String mainProblem;
 @Column(nullable=false,length=20) private String profileStatus="INCOMPLETE";
 @Column(nullable=false,updatable=false) private LocalDateTime createdAt; @Column(nullable=false) private LocalDateTime updatedAt;
 protected ClientProfile(){} public ClientProfile(Account account){this.account=account;}
 @PrePersist void create(){createdAt=updatedAt=LocalDateTime.now();} @PreUpdate void update(){updatedAt=LocalDateTime.now();}
 public UUID getId(){return id;} public Account getAccount(){return account;}
}
