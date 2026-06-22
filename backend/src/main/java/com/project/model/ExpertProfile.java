package com.project.model;
import jakarta.persistence.*; import java.math.BigDecimal; import java.math.RoundingMode; import java.time.LocalDateTime; import java.util.UUID;
@Entity @Table(name="expert_profiles")
public class ExpertProfile {
 @Id @GeneratedValue(strategy=GenerationType.UUID) @Column(name="expert_profile_id") private UUID id;
 @OneToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="account_id",nullable=false,unique=true) private Account account;
 @Column(columnDefinition="nvarchar(max)") private String bio; @Column(nullable=false,columnDefinition="nvarchar(max)") private String aiSkills;
 @Column(nullable=false,length=20) private String skillLevel; private String portfolioLink; @Column(nullable=false) private Integer experienceYears;
 @Column(precision=10,scale=2) private BigDecimal hourlyRate; @Column(nullable=false,precision=3,scale=2) private BigDecimal averageRating=BigDecimal.ZERO;
 @Column(nullable=false) private Integer totalReviews=0; @Column(nullable=false,precision=5,scale=2) private BigDecimal reputationScore=BigDecimal.ZERO;
 @Column(nullable=false,length=20) private String availabilityStatus="AVAILABLE"; @Column(nullable=false,length=20) private String profileStatus="PENDING";
 @Column(nullable=false,updatable=false) private LocalDateTime createdAt; @Column(nullable=false) private LocalDateTime updatedAt;
 protected ExpertProfile(){} public ExpertProfile(Account account){this.account=account;}
 @PrePersist void create(){createdAt=updatedAt=LocalDateTime.now();} @PreUpdate void update(){updatedAt=LocalDateTime.now();}
 public UUID getId(){return id;} public Account getAccount(){return account;} public BigDecimal getAverageRating(){return averageRating;} public Integer getTotalReviews(){return totalReviews;}
 public void addRating(int stars){averageRating=averageRating.multiply(BigDecimal.valueOf(totalReviews)).add(BigDecimal.valueOf(stars)).divide(BigDecimal.valueOf(totalReviews+1),2,RoundingMode.HALF_UP);totalReviews++;}
}
