package com.aitasker.backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aitasker.backend.dto.ProposalRequest;
import com.aitasker.backend.entity.ClientProfile;
import com.aitasker.backend.entity.Escrow;
import com.aitasker.backend.entity.ExpertProfile;
import com.aitasker.backend.entity.Job;
import com.aitasker.backend.entity.Proposal;
import com.aitasker.backend.entity.Transaction;
import com.aitasker.backend.entity.Wallet;
import com.aitasker.backend.repository.EscrowRepository;
import com.aitasker.backend.repository.ExpertProfileRepository;
import com.aitasker.backend.repository.JobRepository;
import com.aitasker.backend.repository.ProposalRepository;
import com.aitasker.backend.repository.TransactionRepository;
import com.aitasker.backend.repository.WalletRepository;

@Service
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final ExpertProfileRepository expertProfileRepository;
    private final JobRepository jobRepository;
    private final EscrowRepository escrowRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;

    public ProposalService(
            ProposalRepository proposalRepository,
            ExpertProfileRepository expertProfileRepository,
            JobRepository jobRepository,
            EscrowRepository escrowRepository,
            WalletRepository walletRepository,
            TransactionRepository transactionRepository,
            NotificationService notificationService
    ) {
        this.proposalRepository = proposalRepository;
        this.expertProfileRepository = expertProfileRepository;
        this.jobRepository = jobRepository;
        this.escrowRepository = escrowRepository;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.notificationService = notificationService;
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

    @Transactional
    public Proposal acceptProposal(Long proposalId) {
        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new RuntimeException("Proposal not found"));

        if (!"SUBMITTED".equalsIgnoreCase(proposal.getProposalStatus())) {
            throw new RuntimeException("Only SUBMITTED proposals can be accepted");
        }

        Job job = proposal.getJob();
        if (!"OPEN".equalsIgnoreCase(job.getJobStatus())) {
            throw new RuntimeException("Job is not open for assignment");
        }

        ClientProfile client = job.getClient();
        Wallet clientWallet = walletRepository.findByUserId(client.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Client wallet not found"));

        BigDecimal price = proposal.getProposalPrice();
        if (clientWallet.getBalance().compareTo(price) < 0) {
            throw new RuntimeException("Insufficient wallet balance to fund escrow");
        }

        clientWallet.setBalance(clientWallet.getBalance().subtract(price));
        walletRepository.save(clientWallet);

        Transaction escrowHoldTx = new Transaction();
        escrowHoldTx.setWallet(clientWallet);
        escrowHoldTx.setAmount(price.negate());
        escrowHoldTx.setTransactionType("ESCROW_HOLD");
        transactionRepository.save(escrowHoldTx);

        Escrow escrow = new Escrow();
        escrow.setJob(job);
        escrow.setClient(client);
        escrow.setExpert(proposal.getExpert());
        escrow.setAmount(price);
        escrow.setEscrowStatus("HELD");
        escrowRepository.save(escrow);

        proposal.setProposalStatus("ACCEPTED");
        proposalRepository.save(proposal);

        List<Proposal> otherProposals = proposalRepository.findByJobJobId(job.getJobId());
        for (Proposal other : otherProposals) {
            if (!other.getProposalId().equals(proposal.getProposalId())) {
                other.setProposalStatus("REJECTED");
                proposalRepository.save(other);
                notificationService.createNotification(
                        other.getExpert().getUser().getId(),
                        "PROPOSAL_REJECTED",
                        "Bao gia cua ban cho cong viec '" + job.getTitle() + "' da bi tu choi.");
            }
        }

        job.setJobStatus("ASSIGNED");
        job.setUpdatedAt(LocalDateTime.now());
        jobRepository.save(job);

        notificationService.createNotification(
                proposal.getExpert().getUser().getId(),
                "PROPOSAL_ACCEPTED",
                "Bao gia cua ban cho cong viec '" + job.getTitle() + "' da duoc chap nhan!");

        return proposal;
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
