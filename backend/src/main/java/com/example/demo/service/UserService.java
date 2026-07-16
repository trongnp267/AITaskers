package com.example.demo.service;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private ClientProfileRepository clientProfileRepository;
    @Autowired private ExpertProfileRepository expertProfileRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    public User authenticate(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("Username does not exist."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("Incorrect password.");
        }

        switch (user.getStatus()) {
            case PENDING:
                throw new BadRequestException(
                        "Your account is awaiting admin approval."
                );

            case REJECTED:
                throw new BadRequestException(
                        "Your account has been rejected."
                );

            case APPROVED:
                return user;

            default:
                throw new BadRequestException("Invalid account status.");
        }
    }

    @Transactional
    public String registerUser(RegisterRequest request) {
        if (request.getPassword() == null || !request.getPassword().equals(request.getConfirmPassword())) {
            return "Passwords do not match!";
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return "Account already exists!";
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(hashedPassword);
        newUser.setRole(request.getRole().toUpperCase());
        newUser.setStatus(AccountStatus.PENDING);
        User savedUser = userRepository.save(newUser);

        if ("CLIENT".equals(savedUser.getRole())) {
            ClientProfile client = new ClientProfile();
            client.setCompanyName(request.getCompanyName());
            client.setIndustry(request.getIndustry());
            client.setCompanySize(request.getCompanySize());
            client.setDescription(request.getDescription());
            client.setUser(savedUser);
            clientProfileRepository.save(client);
        } 
        else if ("EXPERT".equals(savedUser.getRole())) {
            ExpertProfile expert = new ExpertProfile();
            expert.setSkill(request.getSkill());
            expert.setExperience(request.getExperience());
            expert.setCertificate(request.getCertificate());
            expert.setHourlyRate(request.getHourlyRate());
            expert.setUser(savedUser);
            expertProfileRepository.save(expert);
        }

        return "Registration Success!";
    }
}
