package com.aitasker.backend.service;

import com.aitasker.backend.entity.Milestone;
import com.aitasker.backend.repository.MilestoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MilestoneService {

    private final MilestoneRepository milestoneRepository;

    
    public List<Milestone> getMilestonesByProjectId(Long projectId) {
        return milestoneRepository.findByProjectId(projectId);
    }

    
    public Milestone submitMilestone(Long milestoneId) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giai đoạn này!"));
        
        
        milestone.setStatus("WAITING_FOR_APPROVAL");
        return milestoneRepository.save(milestone);
    }
}