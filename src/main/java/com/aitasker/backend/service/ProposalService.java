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
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Expert"));

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy công việc"));

        if (!"OPEN".equalsIgnoreCase(job.getJobStatus())) {
            throw new RuntimeException("Chỉ công việc đang OPEN mới nhận báo giá");
        }

        proposalRepository.findByExpertExpertIdAndJobJobId(
                request.getExpertId(),
                request.getJobId()
        ).ifPresent(existingProposal -> {
            throw new RuntimeException("Bạn đã gửi báo giá cho công việc này rồi");
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
                .orElseThrow(() -> new RuntimeException("Không tìm thấy báo giá"));

        if (!"SUBMITTED".equalsIgnoreCase(proposal.getProposalStatus())) {
            throw new RuntimeException("Chỉ báo giá ở trạng thái SUBMITTED mới chấp nhận được");
        }

        Job job = proposal.getJob();
        if (!"OPEN".equalsIgnoreCase(job.getJobStatus())) {
            throw new RuntimeException("Công việc không còn mở để giao");
        }

        ClientProfile client = job.getClient();
        Wallet clientWallet = walletRepository.findByUserId(client.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví của Client"));

        BigDecimal price = proposal.getProposalPrice();
        if (clientWallet.getBalance().compareTo(price) < 0) {
            throw new RuntimeException("Số dư ví không đủ để ký quỹ — hãy nạp thêm tiền vào ví");
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
                        "Báo giá của bạn cho công việc '" + job.getTitle() + "' đã bị từ chối.");
            }
        }

        job.setJobStatus("ASSIGNED");
        job.setUpdatedAt(LocalDateTime.now());
        jobRepository.save(job);

        notificationService.createNotification(
                proposal.getExpert().getUser().getId(),
                "PROPOSAL_ACCEPTED",
                "Báo giá của bạn cho công việc '" + job.getTitle() + "' đã được chấp nhận!");

        return proposal;
    }

    public List<Proposal> getAllProposals() {
        return proposalRepository.findAll();
    }

    public Proposal getProposalById(Long id) {
        return proposalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy báo giá"));
    }

    public List<Proposal> getProposalsByJobId(Long jobId) {
        return proposalRepository.findByJobJobId(jobId);
    }

    public List<Proposal> getProposalsByExpertId(Long expertId) {
        return proposalRepository.findByExpertExpertId(expertId);
    }

    private void validateProposalRequest(ProposalRequest request) {
        if (request.getExpertId() == null) {
            throw new RuntimeException("Thiếu mã Expert");
        }

        if (request.getJobId() == null) {
            throw new RuntimeException("Thiếu mã công việc");
        }

        if (request.getCoverLetter() == null || request.getCoverLetter().trim().isEmpty()) {
            throw new RuntimeException("Vui lòng viết thư ngỏ");
        }

        if (request.getProposalPrice() == null ||
                request.getProposalPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Giá báo phải lớn hơn 0");
        }

        if (request.getEstimatedDays() == null || request.getEstimatedDays() <= 0) {
            throw new RuntimeException("Số ngày dự kiến phải lớn hơn 0");
        }
    }
}
