package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.JobRequest;
import com.example.demo.entity.ClientProfile;
import com.example.demo.entity.Job;
import com.example.demo.entity.JobSkill;
import com.example.demo.entity.Skill;
import com.example.demo.repository.ClientProfileRepository;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.SkillRepository;

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
                .orElseThrow(() -> new RuntimeException("Client profile not found"));

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
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    private void validateJobRequest(JobRequest request) {
        if (request.getClientId() == null) {
            throw new RuntimeException("Client ID is required");
        }

        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new RuntimeException("Job title is required");
        }

        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new RuntimeException("Job description is required");
        }

        if (request.getPositionRequirement() == null || request.getPositionRequirement().trim().isEmpty()) {
            throw new RuntimeException("Position requirement is required");
        }

        if (request.getJobSkills() == null || request.getJobSkills().isEmpty()) {
            throw new RuntimeException("Job skills are required");
        }

        for (String skill : request.getJobSkills()) {
            if (skill == null || skill.trim().isEmpty()) {
                throw new RuntimeException("Job skill cannot be empty");
            }
        }

        if (request.getMinExperienceYears() == null) {
            throw new RuntimeException("Minimum experience years is required");
        }

        if (request.getMinExperienceYears() < 0) {
            throw new RuntimeException("Minimum experience years must be greater than or equal to 0");
        }

        if (request.getBudgetMin() == null || request.getBudgetMax() == null) {
            throw new RuntimeException("Budget min and budget max are required");
        }

        if (request.getBudgetMin().compareTo(request.getBudgetMax()) > 0) {
            throw new RuntimeException("Budget min must be less than or equal to budget max");
        }

        if (request.getDeadline() == null) {
            throw new RuntimeException("Deadline is required");
        }

        if (request.getDeadline().isBefore(LocalDate.now())) {
            throw new RuntimeException("Deadline must be in the future");
        }
    }
}