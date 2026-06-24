package com.aitasker.backend.service;

import com.aitasker.backend.entity.Escrow;
import com.aitasker.backend.entity.Proposal;
import com.aitasker.backend.repository.EscrowRepository;
import com.aitasker.backend.repository.ProposalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProposalService {
    
    private final ProposalRepository proposalRepository;
    private final EscrowRepository escrowRepository;

    public Proposal acceptProposal(Long proposalId) {
        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ ứng tuyển!"));

        proposal.setStatus("ACCEPTED");
        Proposal savedProposal = proposalRepository.save(proposal);

        Escrow newEscrow = new Escrow();
        newEscrow.setProjectId(proposal.getJobId()); // Ở đây tạm dùng JobID làm ProjectID
        newEscrow.setAmount(BigDecimal.valueOf(proposal.getBidAmount()));
        newEscrow.setStatus("WAITING_FOR_FUND");
        escrowRepository.save(newEscrow);

        return savedProposal;
    }
}