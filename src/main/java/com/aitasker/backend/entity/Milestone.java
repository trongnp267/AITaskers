package com.aitasker.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    // PHAN 6 (Manage Project): TRUOC DAY milestone chi giu "projectId" la mot
    // so Long tro, KHONG co rang buoc khoa ngoai o CSDL -> co the tao milestone
    // tro toi mot job khong ton tai. Doi thanh quan he @ManyToOne toi Job de
    // Hibernate sinh khoa ngoai that (project_id -> Job.job_id). Van giu
    // getProjectId() de tra loi API khong doi (van co field "projectId").
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Job project;

    private String title;
    private String description;
    private BigDecimal amount;
    private String status;
    private LocalDateTime dueDate;

    // Giu nguyen hinh dang JSON cu: van xuat ra "projectId" (la Job.job_id).
    public Long getProjectId() {
        return project != null ? project.getJobId() : null;
    }
}
