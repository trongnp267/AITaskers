package com.example.demo.service;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private ClientProfileRepository clientProfileRepository;
    @Autowired private ExpertProfileRepository expertProfileRepository;
    @Autowired private AiCheckerService aiCheckerService;

    // 1. Logic Đăng nhập
    public String checkLogin(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Trong thực tế, hãy dùng BCryptPasswordEncoder thay vì so sánh chuỗi
            if (user.getPassword().equals(password)) {
                return "Login Success! Role: " + user.getRole();
            }
            return "Invalid password!";
        }
        return "User not found!";
    }

    // 2. Logic Đăng ký (Có sử dụng AI cho Expert)
    @Transactional
    public String registerUser(RegisterRequest request) {
        if (request.getPassword() == null || !request.getPassword().equals(request.getConfirmPassword())) {
            return "Passwords do not match!";
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return "Account already exists!";
        }

        // Tạo User
        User newUser = new User(request.getUsername(), request.getPassword(), request.getRole().toUpperCase());
        User savedUser = userRepository.save(newUser);

        // Phân loại lưu profile
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

            try {
                Map<String, Object> aiResult = aiCheckerService.checkExpertProfile(
                    request.getSkill(), request.getExperience(), request.getCertificate(), request.getHourlyRate()
                );
                expert.setAiScore((Integer) aiResult.get("score"));
                expert.setAiFeedback((String) aiResult.get("feedback"));
            } catch (Exception e) {
                expert.setAiScore(0);
                expert.setAiFeedback("AI evaluation failed.");
            }
            expertProfileRepository.save(expert);
        }

        return "Registration Success!";
    }
}