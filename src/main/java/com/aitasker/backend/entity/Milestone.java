package com.aitasker.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "milestones")
@Data
public class Milestone {

    // TRUOC DAY: @Id nam tren field "proposalId" (dat sai ten) va con mot
    // field "id" khac khong dung toi -> gay nham lan, code cho "milestone.getId()"
    // o cho khac se khong lay dung khoa chinh. Gop lai thanh 1 khoa chinh duy nhat.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "milestone_id")
    private Long id;

    // projectId o day thuc chat la Job ma milestone nay thuoc ve (dung theo
    // MilestoneRepository.findByProjectId / MilestoneController hien tai).
    private Long projectId;

    private String title;
    private String description;
    private BigDecimal amount;
    private String status;
    private LocalDateTime dueDate;
}
