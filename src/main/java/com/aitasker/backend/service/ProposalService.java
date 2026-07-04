package com.aitasker.backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.aitasker.backend.dto.ProposalRequest;
import com.aitasker.backend.entity.ExpertProfile;
import com.aitasker.backend.entity.Job;
import com.aitasker.backend.entity.Proposal;
import com.aitasker.backend.repository.ExpertProfileRepository;
import com.aitasker.backend.repository.JobRepository;
import com.aitasker.backend.repository.ProposalRepository;

@Service
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final ExpertProfileRepository expertProfileRepository;
    private final JobRepository jobRepository;

    public ProposalService(
            ProposalRepository proposalRepository,
            ExpertProfileRepository expertProfileRepository,
            JobRepository jobRepository
    ) {
        this.proposalRepository = proposalRepository;
        this.expertProfileRepository = expertProfileRepository;
        this.jobRepository = jobRepository;
    }

    public Proposal createProposal(ProposalRequest request) {
        validateProposalRequest(request);

        ExpertProfile expert = expertProfileRepository.findById(request.getExpertId())
                .orElseThrow(() -> new RuntimeException("Expert not found"));

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!"OPEN".equalsIgnoreCase(job.getJobStatus())) {
            throw new RuntimeException("Only OPEN jobs can receive proposals");
        }

        proposalRepository.findByExpertExpertIdAndJobJobId(
                request.getExpertId(),
                request.getJobId()
        ).ifPresent(existingProposal -> {
            throw new RuntimeException("This expert has already submitted a proposal for this job");
        });

        Proposal proposal = new Proposal();
        proposal.setExpert(expert);
        proposal.setJob(job);
        proposal.setCoverLetter(request.getCoverLetter());
        proposal.setProposalPrice(request.getProposalPrice());
        proposal.setEstimatedDays(request.getEstimatedDays());
        proposal.setProposalStatus("SUBMITTED");
        proposal.setSubmittedAt(LocalDateTime.now());

        return proposalRepository.save(proposal);
    }

    public List<Proposal> getAllProposals() {
        return proposalRepository.findAll();
    }

    public Proposal getProposalById(Long id) {
        return proposalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proposal not found"));
    }

    public List<Proposal> getProposalsByJobId(Long jobId) {
        return proposalRepository.findByJobJobId(jobId);
    }

    private void validateProposalRequest(ProposalRequest request) {
        if (request.getExpertId() == null) {
            throw new RuntimeException("Expert ID is required");
        }

        if (request.getJobId() == null) {
            throw new RuntimeException("Job ID is required");
        }

        if (request.getCoverLetter() == null || request.getCoverLetter().trim().isEmpty()) {
            throw new RuntimeException("Cover letter is required");
        }

        if (request.getProposalPrice() == null ||
                request.getProposalPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Proposal price must be greater than 0");
        }

        if (request.getEstimatedDays() == null || request.getEstimatedDays() <= 0) {
            throw new RuntimeException("Estimated days must be greater than 0");
        }
    }
}