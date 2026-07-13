package com.aitasker.backend.entity;

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

    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    public JobSkill() {
    }

    public JobSkill(Job job, Skill skill) {
        this.job = job;
        this.skill = skill;
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

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public Long getSkillId() {
        return skill != null ? skill.getSkillId() : null;
    }

    public String getSkillName() {
        return skill != null ? skill.getSkillName() : null;
    }
}
