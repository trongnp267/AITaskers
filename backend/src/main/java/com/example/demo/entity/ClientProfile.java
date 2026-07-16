package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "client_profiles")
public class ClientProfile {
    @Id
    private Long id;

    private String companyName;
    private String industry;
    private String companySize;
    
    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    public ClientProfile() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    public String getCompanySize() { return companySize; }
    public void setCompanySize(String companySize) { this.companySize = companySize; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
