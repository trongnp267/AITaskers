package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ProposalRequest;
import com.example.demo.entity.Proposal;
import com.example.demo.service.ProposalService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/proposals")
@SecurityRequirement(name = "bearerAuth")
public class ProposalController {

    private final ProposalService proposalService;

    public ProposalController(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    @PostMapping
    public Proposal createProposal(@RequestBody ProposalRequest request) {
        return proposalService.createProposal(request);
    }

    @GetMapping
    public List<Proposal> getAllProposals() {
        return proposalService.getAllProposals();
    }

    @GetMapping("/{id}")
    public Proposal getProposalById(@PathVariable Long id) {
        return proposalService.getProposalById(id);
    }

    @GetMapping("/job/{jobId}")
    public List<Proposal> getProposalsByJobId(@PathVariable Long jobId) {
        return proposalService.getProposalsByJobId(jobId);
    }
}