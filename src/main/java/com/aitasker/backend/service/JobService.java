package com.aitasker.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.aitasker.backend.dto.JobRequest;
import com.aitasker.backend.entity.ClientProfile;
import com.aitasker.backend.entity.Job;
import com.aitasker.backend.entity.JobSkill;
import com.aitasker.backend.entity.Skill;
import com.aitasker.backend.repository.ClientProfileRepository;
import com.aitasker.backend.repository.JobRepository;
import com.aitasker.backend.repository.SkillRepository;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final ClientProfileRepository clientProfileRepository;
    private final SkillRepository skillRepository;

    public JobService(
            JobRepository jobRepository,
            ClientProfileRepository clientProfileRepository,
            SkillRepository skillRepository
    ) {
        this.jobRepository = jobRepository;
        this.clientProfileRepository = clientProfileRepository;
        this.skillRepository = skillRepository;
    }

    public Job createJob(JobRequest request) {
        validateJobRequest(request);

        ClientProfile client = clientProfileRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ công ty (Client)"));

        Job job = new Job();

        job.setClient(client);
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setPositionRequirement(request.getPositionRequirement());
        job.setMinExperienceYears(request.getMinExperienceYears());
        job.setBudgetMin(request.getBudgetMin());
        job.setBudgetMax(request.getBudgetMax());
        job.setDeadline(request.getDeadline());

        for (String skillName : request.getJobSkills()) {
            String normalizedSkillName = skillName.trim();

            Skill skill = skillRepository.findBySkillNameIgnoreCase(normalizedSkillName)
                    .orElseGet(() -> skillRepository.save(new Skill(normalizedSkillName, null)));

            JobSkill jobSkill = new JobSkill(job, skill);
            job.getJobSkills().add(jobSkill);
        }

        job.setJobStatus("OPEN");
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());

        return jobRepository.save(job);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy công việc"));
    }

    private void validateJobRequest(JobRequest request) {
        if (request.getClientId() == null) {
            throw new RuntimeException("Thiếu mã Client");
        }

        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new RuntimeException("Vui lòng nhập tên công việc");
        }

        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new RuntimeException("Vui lòng nhập mô tả công việc");
        }

        if (request.getPositionRequirement() == null || request.getPositionRequirement().trim().isEmpty()) {
            throw new RuntimeException("Vui lòng nhập yêu cầu vị trí");
        }

        if (request.getJobSkills() == null || request.getJobSkills().isEmpty()) {
            throw new RuntimeException("Vui lòng thêm ít nhất một kỹ năng");
        }

        for (String skill : request.getJobSkills()) {
            if (skill == null || skill.trim().isEmpty()) {
                throw new RuntimeException("Tên kỹ năng không được để trống");
            }
        }

        if (request.getMinExperienceYears() == null) {
            throw new RuntimeException("Vui lòng nhập số năm kinh nghiệm tối thiểu");
        }

        if (request.getMinExperienceYears() < 0) {
            throw new RuntimeException("Số năm kinh nghiệm phải lớn hơn hoặc bằng 0");
        }

        if (request.getBudgetMin() == null || request.getBudgetMax() == null) {
            throw new RuntimeException("Vui lòng nhập ngân sách tối thiểu và tối đa");
        }

        if (request.getBudgetMin().compareTo(request.getBudgetMax()) > 0) {
            throw new RuntimeException("Ngân sách tối thiểu phải nhỏ hơn hoặc bằng ngân sách tối đa");
        }

        if (request.getDeadline() == null) {
            throw new RuntimeException("Vui lòng chọn ngày hết hạn");
        }

        if (request.getDeadline().isBefore(LocalDate.now())) {
            throw new RuntimeException("Ngày hết hạn phải ở tương lai");
        }
    }
}
