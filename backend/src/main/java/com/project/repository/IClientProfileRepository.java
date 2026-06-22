package com.project.repository; import com.project.model.ClientProfile; import java.util.UUID; import org.springframework.data.jpa.repository.JpaRepository;
public interface IClientProfileRepository extends JpaRepository<ClientProfile,UUID>{}
