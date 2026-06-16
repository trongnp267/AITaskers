package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "expert_profiles")
public class ExpertProfile {
    @Id
    private Long id; 

    private String skill;
    private String experience;
    private String certificate;
    private Double hourlyRate;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    public ExpertProfile() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSkill() { return skill; }
    public void setSkill(String skill) { this.skill = skill; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
    public String getCertificate() { return certificate; }
    public void setCertificate(String certificate) { this.certificate = certificate; }
    public Double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(Double hourlyRate) { this.hourlyRate = hourlyRate; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}