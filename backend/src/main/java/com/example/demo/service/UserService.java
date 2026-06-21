package com.example.demo.service;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private ClientProfileRepository clientProfileRepository;
    @Autowired private ExpertProfileRepository expertProfileRepository;
    
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    public String checkLogin(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return "Login Success! Role: " + user.getRole();
            }
            return "Invalid password!";
        }
        return "User not found!";
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
        
        User newUser = new User(request.getUsername(), hashedPassword, request.getRole().toUpperCase());
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