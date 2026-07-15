package com.aitasker.backend.service;

import com.aitasker.backend.dto.MilestoneRequest;
import com.aitasker.backend.entity.Escrow;
import com.aitasker.backend.entity.Job;
import com.aitasker.backend.entity.Milestone;
import com.aitasker.backend.entity.Transaction;
import com.aitasker.backend.entity.Wallet;
import com.aitasker.backend.repository.EscrowRepository;
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

    public List<Milestone> getMilestonesByProjectId(Long projectId) {
        return milestoneRepository.findByProject_JobId(projectId);
    }

    /**
     * PHAN 6 (Manage Project): TRUOC DAY khong co endpoint/ham nao tao milestone
     * (chi co list + submit), nen thuc te khong co cach nao them giai doan cho
     * mot Job qua API. Bo sung tao milestone gan voi mot Job co that (khoa
     * ngoai project_id -> Job.job_id).
     */
    public Milestone createMilestone(MilestoneRequest request) {
        validateMilestoneRequest(request);

        Job job = jobRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Khong tim thay cong viec (Job) cho milestone nay"));

        Milestone milestone = new Milestone();
        milestone.setProject(job);
        milestone.setTitle(request.getTitle());
        milestone.setDescription(request.getDescription());
        milestone.setAmount(request.getAmount());
        milestone.setDueDate(request.getDueDate());
        milestone.setStatus("PENDING");

        return milestoneRepository.save(milestone);
    }

    /**
     * PHAN 6 (Manage Project): cap nhat thong tin mot milestone (tieu de, mo ta,
     * so tien, han). Chi cho phep sua khi milestone chua duoc duyet/giai ngan.
     */
    public Milestone updateMilestone(Long milestoneId, MilestoneRequest request) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new RuntimeException("Khong tim thay giai doan nay!"));

        if ("APPROVED".equalsIgnoreCase(milestone.getStatus())) {
            throw new RuntimeException("Khong the sua giai doan da duoc duyet/giai ngan");
        }

        if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
            milestone.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            milestone.setDescription(request.getDescription());
        }
        if (request.getAmount() != null) {
            if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("So tien giai doan phai lon hon 0");
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
            throw new RuntimeException("projectId (job id) la bat buoc");
        }
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new RuntimeException("Tieu de milestone la bat buoc");
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("So tien milestone phai lon hon 0");
        }
    }

    public Milestone submitMilestone(Long milestoneId) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new RuntimeException("Khong tim thay giai doan nay!"));

        milestone.setStatus("WAITING_FOR_APPROVAL");
        return milestoneRepository.save(milestone);
    }

    /**
     * TRUOC DAY: chua co buoc "duyet" nao sau khi Expert submit deliverable
     * (submitMilestone chi doi status sang WAITING_FOR_APPROVAL roi dung lai).
     * Client khong co cach nao giai ngan tien tu Escrow sang vi Expert - du
     * Transaction.java da chua san loai "ESCROW_RELEASE" nhung chua noi nao
     * dung toi. Bo sung day du quy trinh duyet mo ta o Escrow/Transaction:
     * 1. Milestone phai dang WAITING_FOR_APPROVAL.
     * 2. Escrow cua Job phai dang HELD va con du tien >= amount cua milestone.
     * 3. Chuyen amount tu Escrow sang vi Expert, ghi lai Transaction ESCROW_RELEASE.
     * 4. Tru amount da giai ngan khoi Escrow; het tien thi Escrow -> RELEASED.
     * 5. Milestone -> APPROVED; neu tat ca milestone cua Job da APPROVED thi
     *    Job -> COMPLETED.
     */
    @Transactional
    public Milestone approveMilestone(Long milestoneId) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new RuntimeException("Khong tim thay giai doan nay!"));

        if (!"WAITING_FOR_APPROVAL".equalsIgnoreCase(milestone.getStatus())) {
            throw new RuntimeException("Chi co the duyet giai doan dang o trang thai cho duyet");
        }

        Escrow escrow = escrowRepository.findByJobJobId(milestone.getProjectId())
                .orElseThrow(() -> new RuntimeException("Khong tim thay quy ky quy (Escrow) cua cong viec nay"));

        if (!"HELD".equalsIgnoreCase(escrow.getEscrowStatus())) {
            throw new RuntimeException("Quy ky quy khong o trang thai HELD, khong the giai ngan");
        }

        BigDecimal amount = milestone.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("So tien giai doan khong hop le");
        }

        if (escrow.getAmount().compareTo(amount) < 0) {
            throw new RuntimeException("So tien con lai trong quy ky quy khong du de giai ngan giai doan nay");
        }

        // 1. Chuyen tien tu Escrow sang vi Expert
        Wallet expertWallet = walletRepository.findByUserId(escrow.getExpert().getUser().getId())
                .orElseThrow(() -> new RuntimeException("Khong tim thay vi cua Expert"));

        expertWallet.setBalance(expertWallet.getBalance().add(amount));
        walletRepository.save(expertWallet);

        Transaction releaseTx = new Transaction();
        releaseTx.setWallet(expertWallet);
        releaseTx.setAmount(amount);
        releaseTx.setTransactionType("ESCROW_RELEASE");
        transactionRepository.save(releaseTx);

        // 2. Tru tien da giai ngan khoi Escrow
        escrow.setAmount(escrow.getAmount().subtract(amount));
        if (escrow.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            escrow.setEscrowStatus("RELEASED");
        }
        escrow.setUpdatedAt(LocalDateTime.now());
        escrowRepository.save(escrow);

        // 3. Danh dau Milestone da duoc duyet
        milestone.setStatus("APPROVED");
        milestoneRepository.save(milestone);

        // 4. Neu tat ca milestone cua Job da APPROVED thi Job hoan thanh
        List<Milestone> allMilestones = milestoneRepository.findByProject_JobId(milestone.getProjectId());
        boolean allApproved = allMilestones.stream()
                .allMatch(m -> "APPROVED".equalsIgnoreCase(m.getStatus()));

        if (allApproved) {
            Job job = jobRepository.findById(milestone.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Khong tim thay cong viec"));
            job.setJobStatus("COMPLETED");
            job.setUpdatedAt(LocalDateTime.now());
            jobRepository.save(job);
        }

        return milestone;
    }

    /**
     * Client tu choi san pham da nop: khong dong tien nao trong Escrow bi anh
     * huong (van HELD), Milestone quay ve REJECTED de Expert nop lai.
     */
    public Milestone rejectMilestone(Long milestoneId) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new RuntimeException("Khong tim thay giai doan nay!"));

        if (!"WAITING_FOR_APPROVAL".equalsIgnoreCase(milestone.getStatus())) {
            throw new RuntimeException("Chi co the tu choi giai doan dang o trang thai cho duyet");
        }

        milestone.setStatus("REJECTED");
        return milestoneRepository.save(milestone);
    }
}
