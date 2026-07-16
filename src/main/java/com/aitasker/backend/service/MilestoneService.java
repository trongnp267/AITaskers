package com.aitasker.backend.service;

import com.aitasker.backend.dto.MilestoneRequest;
import com.aitasker.backend.entity.Escrow;
import com.aitasker.backend.entity.ExpertProfile;
import com.aitasker.backend.entity.Job;
import com.aitasker.backend.entity.Milestone;
import com.aitasker.backend.entity.Transaction;
import com.aitasker.backend.entity.Wallet;
import com.aitasker.backend.repository.EscrowRepository;
import com.aitasker.backend.repository.ExpertProfileRepository;
import com.aitasker.backend.repository.JobRepository;
import com.aitasker.backend.repository.MilestoneRepository;
import com.aitasker.backend.repository.TransactionRepository;
import com.aitasker.backend.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final EscrowRepository escrowRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final JobRepository jobRepository;
    private final ExpertProfileRepository expertProfileRepository;
    private final NotificationService notificationService;

    public List<Milestone> getMilestonesByProjectId(Long projectId) {
        return milestoneRepository.findByProject_JobId(projectId);
    }

    public Milestone createMilestone(MilestoneRequest request) {
        validateMilestoneRequest(request);

        Job job = jobRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy công việc cho milestone này"));

        Milestone milestone = new Milestone();
        milestone.setProject(job);
        milestone.setTitle(request.getTitle());
        milestone.setDescription(request.getDescription());
        milestone.setAmount(request.getAmount());
        milestone.setDueDate(request.getDueDate());
        milestone.setStatus("PENDING");

        return milestoneRepository.save(milestone);
    }

    public Milestone updateMilestone(Long milestoneId, MilestoneRequest request) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giai đoạn này!"));

        if ("APPROVED".equalsIgnoreCase(milestone.getStatus())) {
            throw new RuntimeException("Không thể sửa giai đoạn đã được duyệt/giải ngân");
        }

        if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
            milestone.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            milestone.setDescription(request.getDescription());
        }
        if (request.getAmount() != null) {
            if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Số tiền giai đoạn phải lớn hơn 0");
            }
            milestone.setAmount(request.getAmount());
        }
        if (request.getDueDate() != null) {
            milestone.setDueDate(request.getDueDate());
        }

        return milestoneRepository.save(milestone);
    }

    private void validateMilestoneRequest(MilestoneRequest request) {
        if (request.getProjectId() == null) {
            throw new RuntimeException("projectId (mã công việc) là bắt buộc");
        }
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new RuntimeException("Tiêu đề milestone là bắt buộc");
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Số tiền milestone phải lớn hơn 0");
        }
    }

    public Milestone submitMilestone(Long milestoneId) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giai đoạn này!"));

        milestone.setStatus("WAITING_FOR_APPROVAL");
        return milestoneRepository.save(milestone);
    }

    @Transactional
    public Milestone approveMilestone(Long milestoneId) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giai đoạn này!"));

        if (!"WAITING_FOR_APPROVAL".equalsIgnoreCase(milestone.getStatus())) {
            throw new RuntimeException("Chỉ có thể duyệt giai đoạn đang ở trạng thái chờ duyệt");
        }

        Escrow escrow = escrowRepository.findByJobJobId(milestone.getProjectId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quỹ ký quỹ (Escrow) của công việc này"));

        if (!"HELD".equalsIgnoreCase(escrow.getEscrowStatus())) {
            throw new RuntimeException("Quỹ ký quỹ không ở trạng thái HELD, không thể giải ngân");
        }

        BigDecimal amount = milestone.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Số tiền giai đoạn không hợp lệ");
        }

        if (escrow.getAmount().compareTo(amount) < 0) {
            throw new RuntimeException("Số tiền còn lại trong quỹ ký quỹ không đủ để giải ngân giai đoạn này");
        }

        Wallet expertWallet = walletRepository.findByUserId(escrow.getExpert().getUser().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví của Expert"));

        expertWallet.setBalance(expertWallet.getBalance().add(amount));
        walletRepository.save(expertWallet);

        Transaction releaseTx = new Transaction();
        releaseTx.setWallet(expertWallet);
        releaseTx.setAmount(amount);
        releaseTx.setTransactionType("ESCROW_RELEASE");
        transactionRepository.save(releaseTx);

        escrow.setAmount(escrow.getAmount().subtract(amount));
        if (escrow.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            escrow.setEscrowStatus("RELEASED");
        }
        escrow.setUpdatedAt(LocalDateTime.now());
        escrowRepository.save(escrow);

        milestone.setStatus("APPROVED");
        milestoneRepository.save(milestone);

        notificationService.createNotification(
                escrow.getExpert().getUser().getId(),
                "MILESTONE_APPROVED",
                "Giai đoạn '" + milestone.getTitle() + "' đã được duyệt và giải ngân " + amount);

        List<Milestone> allMilestones = milestoneRepository.findByProject_JobId(milestone.getProjectId());
        boolean allApproved = allMilestones.stream()
                .allMatch(m -> "APPROVED".equalsIgnoreCase(m.getStatus()));

        if (allApproved) {
            Job job = jobRepository.findById(milestone.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy công việc"));
            job.setJobStatus("COMPLETED");
            job.setUpdatedAt(LocalDateTime.now());
            jobRepository.save(job);

            ExpertProfile expert = escrow.getExpert();
            Integer completed = expert.getCompletedJobs() == null ? 0 : expert.getCompletedJobs();
            expert.setCompletedJobs(completed + 1);
            expertProfileRepository.save(expert);
        }

        return milestone;
    }

    public Milestone rejectMilestone(Long milestoneId) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giai đoạn này!"));

        if (!"WAITING_FOR_APPROVAL".equalsIgnoreCase(milestone.getStatus())) {
            throw new RuntimeException("Chỉ có thể từ chối giai đoạn đang ở trạng thái chờ duyệt");
        }

        milestone.setStatus("REJECTED");
        return milestoneRepository.save(milestone);
    }
}
