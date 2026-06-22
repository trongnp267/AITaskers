package com.project.repository; import com.project.model.Project; import java.util.UUID; import org.springframework.data.jpa.repository.JpaRepository;
public interface IProjectRepository extends JpaRepository<Project,UUID>{}
