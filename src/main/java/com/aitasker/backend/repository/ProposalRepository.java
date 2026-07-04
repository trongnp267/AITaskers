package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Proposal;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {

    List<Proposal> findByJobJobId(Long jobId);

    Optional<Proposal> findByExpertExpertIdAndJobJobId(Long expertId, Long jobId);
}