package com.example.demo.service;

import com.example.demo.entity.AccountStatus;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.findByUsername("admin").isEmpty()) {

            User admin = new User();

            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRole("ADMIN");
            admin.setStatus(AccountStatus.APPROVED);

            userRepository.save(admin);

            System.out.println("Admin account created.");
        }

    }
}
