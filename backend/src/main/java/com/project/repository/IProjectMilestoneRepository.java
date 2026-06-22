package com.project.repository; import com.project.model.ProjectMilestone; import java.util.UUID; import org.springframework.data.jpa.repository.JpaRepository;
public interface IProjectMilestoneRepository extends JpaRepository<ProjectMilestone,UUID>{}
