package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.JobSkill;

public interface JobSkillRepository extends JpaRepository<JobSkill, Long> {
}