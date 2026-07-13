package com.aitasker.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aitasker.backend.entity.JobSkill;

public interface JobSkillRepository extends JpaRepository<JobSkill, Long> {
}
