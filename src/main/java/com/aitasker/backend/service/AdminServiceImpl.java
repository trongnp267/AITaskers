package com.aitasker.backend.service;

import com.aitasker.backend.dto.PendingAccountResponse;
import com.aitasker.backend.entity.AccountStatus;
import com.aitasker.backend.entity.User;
import com.aitasker.backend.exception.ResourceNotFoundException;
import com.aitasker.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
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
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        return toResponse(user);
    }

    @Override
    public void approveAccount(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        user.setStatus(AccountStatus.APPROVED);
        userRepository.save(user);
        notificationService.createNotification(
                user.getId(),
                "ACCOUNT_APPROVED",
                "Tai khoan cua ban da duoc admin phe duyet. Ban co the dang nhap.");
    }

    @Override
    public void rejectAccount(Long id, String reason) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        user.setStatus(AccountStatus.REJECTED);
        userRepository.save(user);
        notificationService.createNotification(
                user.getId(),
                "ACCOUNT_REJECTED",
                "Tai khoan cua ban da bi tu choi." + (reason != null && !reason.isBlank() ? " Ly do: " + reason : ""));
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
