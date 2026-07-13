package com.aitasker.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aitasker.backend.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
}
