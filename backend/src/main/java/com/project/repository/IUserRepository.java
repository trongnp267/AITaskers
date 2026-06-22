package com.project.repository;

import com.project.model.Account;
import java.util.UUID;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByEmail(String email);
}
