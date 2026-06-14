package com.example.demo.service;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.ClientProfile;
import com.example.demo.entity.ExpertProfile;
import com.example.demo.entity.User;
import com.example.demo.repository.ClientProfileRepository;
import com.example.demo.repository.ExpertProfileRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientProfileRepository clientProfileRepository;

    @Autowired
    private ExpertProfileRepository expertProfileRepository;

    public String checkLogin(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                return "Login Success! Role: " + user.getRole();
            } else {
                return "Invalid password!";
            }
        }
        return "User not found!";
    }

    @Transactional // Đảm bảo nếu lỗi lưu Profile thì User cũng sẽ rollback (không bị rác dữ liệu)
    public String registerUser(RegisterRequest request) {
        if (request.getPassword() == null || !request.getPassword().equals(request.getConfirmPassword())) {
            return "Passwords do not match!";
        }

        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            return "Account already exists!";
        }

        // 1. Tạo và lưu User chính trước
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(request.getPassword());
        
        // Chuẩn hóa role nhập vào thành chữ hoa (CLIENT hoặc EXPERT)
        String role = (request.getRole() != null) ? request.getRole().toUpperCase().trim() : "CLIENT";
        if (!role.equals("CLIENT") && !role.equals("EXPERT")) {
            return "Invalid Role! Please choose CLIENT or EXPERT.";
        }
        newUser.setRole(role);

        // Lưu user để DB sinh ID tự động tăng
        User savedUser = userRepository.save(newUser);

        // 2. Kiểm tra Role để phân nhánh lưu vào bảng Profile tương ứng
        if (role.equals("CLIENT")) {
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setCompanyName(request.getCompanyName());
            clientProfile.setIndustry(request.getIndustry());
            clientProfile.setCompanySize(request.getCompanySize());
            clientProfile.setDescription(request.getDescription());
            clientProfile.setUser(savedUser); // Gán mối quan hệ liên kết
            
            clientProfileRepository.save(clientProfile);
        } else if (role.equals("EXPERT")) {
            ExpertProfile expertProfile = new ExpertProfile();
            expertProfile.setSkill(request.getSkill());
            expertProfile.setExperience(request.getExperience());
            expertProfile.setCertificate(request.getCertificate());
            expertProfile.setHourlyRate(request.getHourlyRate());
            expertProfile.setUser(savedUser); // Gán mối quan hệ liên kết
            
            expertProfileRepository.save(expertProfile);
        }

        return "Registration Success!";
    }
}