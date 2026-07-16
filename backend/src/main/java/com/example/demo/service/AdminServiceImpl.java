package com.example.demo.service;

import com.example.demo.dto.PendingAccountResponse;
import com.example.demo.entity.AccountStatus;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

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
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        return toResponse(user);

    }

    @Override
    public void approveAccount(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        user.setStatus(AccountStatus.APPROVED);

        userRepository.save(user);

    }

    @Override
    public void rejectAccount(Long id, String reason) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        user.setStatus(AccountStatus.REJECTED);

        // nếu có field rejectReason
        // user.setRejectReason(reason);

        userRepository.save(user);

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
