package com.aitasker.backend.service;

import com.aitasker.backend.dto.PendingAccountResponse;
import com.aitasker.backend.entity.AccountStatus;
import com.aitasker.backend.entity.User;
import com.aitasker.backend.exception.ResourceNotFoundException;
import com.aitasker.backend.repository.EscrowRepository;
import com.aitasker.backend.repository.JobRepository;
import com.aitasker.backend.repository.ProposalRepository;
import com.aitasker.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ProposalRepository proposalRepository;
    private final EscrowRepository escrowRepository;
    private final NotificationService notificationService;

    @Override
    public List<PendingAccountResponse> getPendingAccounts() {
        return userRepository.findByStatus(AccountStatus.PENDING)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public PendingAccountResponse getAccount(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng."));
        return toResponse(user);
    }

    @Override
    public void approveAccount(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng."));
        user.setStatus(AccountStatus.APPROVED);
        userRepository.save(user);
        notificationService.createNotification(
                user.getId(),
                "ACCOUNT_APPROVED",
                "Tài khoản của bạn đã được admin phê duyệt. Bạn có thể đăng nhập.");
    }

    @Override
    public void rejectAccount(Long id, String reason) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng."));
        user.setStatus(AccountStatus.REJECTED);
        userRepository.save(user);
        notificationService.createNotification(
                user.getId(),
                "ACCOUNT_REJECTED",
                "Tài khoản của bạn đã bị từ chối." + (reason != null && !reason.isBlank() ? " Lý do: " + reason : ""));
    }

    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("pendingAccounts", userRepository.findByStatus(AccountStatus.PENDING).size());
        stats.put("totalJobs", jobRepository.count());
        stats.put("totalProposals", proposalRepository.count());
        stats.put("totalEscrows", escrowRepository.count());
        return stats;
    }

    private PendingAccountResponse toResponse(User user) {
        return new PendingAccountResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }
}
