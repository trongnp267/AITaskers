package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.ProposalRequest;
import com.example.demo.entity.ExpertProfile;
import com.example.demo.entity.Job;
import com.example.demo.entity.Proposal;
import com.example.demo.repository.ExpertProfileRepository;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.ProposalRepository;

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
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Expert not found"
                ));

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Job not found"
                ));

        if (!"OPEN".equalsIgnoreCase(job.getJobStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only OPEN jobs can receive proposals"
            );
        }

        proposalRepository.findByExpertIdAndJobJobId(
                request.getExpertId(),
                request.getJobId()
        ).ifPresent(existingProposal -> {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This expert has already submitted a proposal for this job"
            );
        });

        if (job.getBudgetMax() != null &&
                request.getProposalPrice().compareTo(job.getBudgetMax()) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Proposal price must not exceed the job maximum budget"
            );
        }

        if (job.getDeadline() != null) {
            long remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), job.getDeadline());

            if (remainingDays < 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Job deadline has already passed"
                );
            }

            if (request.getEstimatedDays() > remainingDays) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Estimated days must not exceed the remaining days before job deadline"
                );
            }
        }

        Proposal proposal = new Proposal();
        proposal.setExpert(expert);
        proposal.setJob(job);
        proposal.setCoverLetter(request.getCoverLetter().trim());
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
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Proposal not found"
                ));
    }

    public List<Proposal> getProposalsByJobId(Long jobId) {
        return proposalRepository.findByJobJobId(jobId);
    }

    private void validateProposalRequest(ProposalRequest request) {
        if (request.getExpertId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Expert ID is required"
            );
        }

        if (request.getJobId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Job ID is required"
            );
        }

        if (request.getCoverLetter() == null || request.getCoverLetter().trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cover letter is required"
            );
        }

        if (request.getCoverLetter().trim().length() < 30) {
            throw new ResponseStatusException(
                     HttpStatus.BAD_REQUEST,
                    "Cover letter must be at least 30 characters"
            );
        }

        if (request.getProposalPrice() == null ||
                request.getProposalPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Proposal price must be greater than 0"
            );
        }

        if (request.getEstimatedDays() == null || request.getEstimatedDays() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Estimated days must be greater than 0"
            );
        }
    }
}
