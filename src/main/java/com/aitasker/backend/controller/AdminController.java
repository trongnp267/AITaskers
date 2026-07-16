package com.aitasker.backend.controller;

import com.aitasker.backend.dto.PendingAccountResponse;
import com.aitasker.backend.dto.RejectRequest;
import com.aitasker.backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/admin/accounts")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/pending")
    public ResponseEntity<List<PendingAccountResponse>> getPendingAccounts() {
        return ResponseEntity.ok(adminService.getPendingAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PendingAccountResponse> getAccount(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getAccount(id));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approveAccount(@PathVariable Long id) {
        adminService.approveAccount(id);
        return ResponseEntity.ok("Account approved successfully.");
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<String> rejectAccount(@PathVariable Long id,
                                                @RequestBody RejectRequest request) {
        adminService.rejectAccount(id, request.getReason());
        return ResponseEntity.ok("Account rejected successfully.");
    }
}
