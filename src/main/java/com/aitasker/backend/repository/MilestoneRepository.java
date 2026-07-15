package com.aitasker.backend.repository;

import com.aitasker.backend.entity.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, Long> {

    // PHAN 6: milestone.project la quan he @ManyToOne toi Job, nen loc theo
    // job id qua nested-property project.jobId.
    List<Milestone> findByProject_JobId(Long jobId);
}
