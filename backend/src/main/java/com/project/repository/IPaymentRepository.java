package com.project.repository;
import com.project.model.Payment; import java.util.UUID; import org.springframework.data.jpa.repository.JpaRepository;
public interface IPaymentRepository extends JpaRepository<Payment, UUID> {}
