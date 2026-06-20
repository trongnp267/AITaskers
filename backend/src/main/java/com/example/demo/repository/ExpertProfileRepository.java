package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ExpertProfile;

public interface ExpertProfileRepository extends JpaRepository<ExpertProfile, Long> {
}