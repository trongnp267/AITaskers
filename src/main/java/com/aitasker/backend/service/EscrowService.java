package com.aitasker.backend.service;

import com.aitasker.backend.entity.Escrow;
import com.aitasker.backend.entity.ExpertProfile;
import com.aitasker.backend.entity.Job;
import com.aitasker.backend.entity.Transaction;
import com.aitasker.backend.entity.Wallet;
import com.aitasker.backend.exception.BadRequestException;
import com.aitasker.backend.exception.ResourceNotFoundException;
import com.aitasker.backend.repository.EscrowRepository;
import com.aitasker.backend.repository.ExpertProfileRepository;
import com.aitasker.backend.repository.JobRepository;
import com.aitasker.backend.repository.TransactionRepository;
import com.aitasker.backend.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class EscrowService {

    private final EscrowRepository escrowRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final JobRepository jobRepository;
    private final ExpertProfileRepository expertProfileRepository;
    private final NotificationService notificationService;

    public EscrowService(EscrowRepository escrowRepository,
                         WalletRepository walletRepository,
                         TransactionRepository transactionRepository,
                         JobRepository jobRepository,
                         ExpertProfileRepository expertProfileRepository,
                         NotificationService notificationService) {
        this.escrowRepository = escrowRepository;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.jobRepository = jobRepository;
        this.expertProfileRepository = expertProfileRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public Escrow releaseEscrow(Long escrowId) {
        Escrow escrow = escrowRepository.findById(escrowId)
                .orElseThrow(() -> new ResourceNotFoundException("Escrow not found"));

        if (!"HELD".equalsIgnoreCase(escrow.getEscrowStatus())) {
            throw new BadRequestException("Only HELD escrows can be released");
        }

        BigDecimal amount = escrow.getAmount();

        Wallet expertWallet = walletRepository.findByUserId(escrow.getExpert().getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Expert wallet not found"));
        expertWallet.setBalance(expertWallet.getBalance().add(amount));
        walletRepository.save(expertWallet);

        Transaction tx = new Transaction();
        tx.setWallet(expertWallet);
        tx.setAmount(amount);
        tx.setTransactionType("ESCROW_RELEASE");
        transactionRepository.save(tx);

        escrow.setEscrowStatus("RELEASED");
        escrow.setUpdatedAt(LocalDateTime.now());
        escrowRepository.save(escrow);

        Job job = escrow.getJob();
        job.setJobStatus("COMPLETED");
        job.setUpdatedAt(LocalDateTime.now());
        jobRepository.save(job);

        ExpertProfile expert = escrow.getExpert();
        Integer completed = expert.getCompletedJobs() == null ? 0 : expert.getCompletedJobs();
        expert.setCompletedJobs(completed + 1);
        expertProfileRepository.save(expert);

        notificationService.createNotification(
                expert.getUser().getId(),
                "ESCROW_RELEASED",
                "Ban da nhan " + amount + " tu quy ky quy cua cong viec '" + job.getTitle() + "'.");

        return escrow;
    }

    @Transactional
    public Escrow refundEscrow(Long escrowId) {
        Escrow escrow = escrowRepository.findById(escrowId)
                .orElseThrow(() -> new ResourceNotFoundException("Escrow not found"));

        if (!"HELD".equalsIgnoreCase(escrow.getEscrowStatus())) {
            throw new BadRequestException("Only HELD escrows can be refunded");
        }

        BigDecimal amount = escrow.getAmount();

        Wallet clientWallet = walletRepository.findByUserId(escrow.getClient().getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Client wallet not found"));
        clientWallet.setBalance(clientWallet.getBalance().add(amount));
        walletRepository.save(clientWallet);

        Transaction tx = new Transaction();
        tx.setWallet(clientWallet);
        tx.setAmount(amount);
        tx.setTransactionType("ESCROW_REFUND");
        transactionRepository.save(tx);

        escrow.setEscrowStatus("REFUNDED");
        escrow.setUpdatedAt(LocalDateTime.now());
        escrowRepository.save(escrow);

        Job job = escrow.getJob();
        job.setJobStatus("CANCELLED");
        job.setUpdatedAt(LocalDateTime.now());
        jobRepository.save(job);

        notificationService.createNotification(
                escrow.getClient().getUser().getId(),
                "ESCROW_REFUNDED",
                "Ban da duoc hoan " + amount + " tu quy ky quy cua cong viec '" + job.getTitle() + "'.");

        return escrow;
    }
}
