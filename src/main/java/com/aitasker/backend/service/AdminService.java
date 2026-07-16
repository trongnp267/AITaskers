package com.aitasker.backend.service;

import com.aitasker.backend.dto.PendingAccountResponse;

import java.util.List;
import java.util.Map;

public interface AdminService {
    List<PendingAccountResponse> getPendingAccounts();

    PendingAccountResponse getAccount(Long id);

    void approveAccount(Long id);

    void rejectAccount(Long id, String reason);

    Map<String, Object> getStats();
}
