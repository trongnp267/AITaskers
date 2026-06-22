package com.project.repository;
import com.project.model.ExpertProfile; import java.util.UUID; import org.springframework.data.jpa.repository.JpaRepository;
public interface IExpertProfileRepository extends JpaRepository<ExpertProfile, UUID> {}
