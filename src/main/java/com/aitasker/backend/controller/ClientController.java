package com.aitasker.backend.controller;

import com.aitasker.backend.entity.ClientProfile;
import com.aitasker.backend.exception.ResourceNotFoundException;
import com.aitasker.backend.repository.ClientProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientProfileRepository clientProfileRepository;

    public ClientController(ClientProfileRepository clientProfileRepository) {
        this.clientProfileRepository = clientProfileRepository;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllClients() {
        List<Map<String, Object>> result = clientProfileRepository.findAll()
                .stream().map(this::toResponse).toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getClient(@PathVariable Long id) {
        ClientProfile client = clientProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        return ResponseEntity.ok(toResponse(client));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateClient(@PathVariable Long id,
                                                            @RequestBody Map<String, Object> body) {
        ClientProfile client = clientProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        if (body.get("companyName") != null) {
            client.setCompanyName(String.valueOf(body.get("companyName")));
        }
        if (body.get("description") != null) {
            client.setDescription(String.valueOf(body.get("description")));
        }
        clientProfileRepository.save(client);
        return ResponseEntity.ok(toResponse(client));
    }

    private Map<String, Object> toResponse(ClientProfile client) {
        Map<String, Object> map = new HashMap<>();
        map.put("clientId", client.getClientId());
        map.put("companyName", client.getCompanyName());
        map.put("description", client.getDescription());
        map.put("username", client.getUser() != null ? client.getUser().getUsername() : null);
        map.put("userId", client.getUser() != null ? client.getUser().getId() : null);
        map.put("createdAt", client.getCreatedAt());
        return map;
    }
}
