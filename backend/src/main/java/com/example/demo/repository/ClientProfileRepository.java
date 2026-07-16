package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.ClientProfile;

public interface ClientProfileRepository extends JpaRepository<ClientProfile, Long> {
}