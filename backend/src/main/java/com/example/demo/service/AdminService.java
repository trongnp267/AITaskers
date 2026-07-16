package com.example.demo.service;

import com.example.demo.dto.PendingAccountResponse;

import java.util.List;

public interface AdminService {
    List<PendingAccountResponse> getPendingAccounts();

    PendingAccountResponse getAccount(Long id);

    void approveAccount(Long id);

    void rejectAccount(Long id, String reason);
}
