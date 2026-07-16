package com.example.demo.controller;

import com.example.demo.dto.PendingAccountResponse;
import com.example.demo.dto.RejectRequest;
import com.example.demo.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/accounts")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/pending")
    public ResponseEntity<List<PendingAccountResponse>> getPendingAccounts() {

        return ResponseEntity.ok(
                adminService.getPendingAccounts()
        );

    }

    @GetMapping("/{id}")
    public ResponseEntity<PendingAccountResponse> getAccount(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                adminService.getAccount(id)
        );

    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approveAccount(
            @PathVariable Long id
    ) {

        adminService.approveAccount(id);

        return ResponseEntity.ok(
                "Account approved successfully."
        );

    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<String> rejectAccount(

            @PathVariable Long id,

            @RequestBody RejectRequest request

    ) {

        adminService.rejectAccount(id, request.getReason());

        return ResponseEntity.ok(
                "Account rejected successfully."
        );

    }

}
