package com.project.repository;

import com.project.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
