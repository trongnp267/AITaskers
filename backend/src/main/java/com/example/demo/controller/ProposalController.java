package com.example.demo.controller;

import com.example.demo.dto.ProposalRequest;
import com.example.demo.entity.Proposal;
import com.example.demo.service.ProposalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/proposals")
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