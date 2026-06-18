package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "job_skills")
public class JobSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_skill_id")
    private Long jobSkillId;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    @JsonIgnore
    private Job job;

    @Column(name = "skill_name", nullable = false)
    private String skillName;

    public JobSkill() {
    }

    public JobSkill(Job job, String skillName) {
        this.job = job;
        this.skillName = skillName;
    }

    public Long getJobSkillId() {
        return jobSkillId;
    }

    public void setJobSkillId(Long jobSkillId) {
        this.jobSkillId = jobSkillId;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
}