package com.example.demo.service;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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
        return "Email not registered!"; // Đã cập nhật cho phù hợp với ngữ cảnh email
    }

    // --- BỔ SUNG LOGIC ĐĂNG KÝ THEO SƠ ĐỒ SWIMLANE ---
    public String registerUser(RegisterRequest request) {
        // 1. Kiểm tra Password và Confirm Password có khớp nhau không (Frontend validate, Backend check lại)
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return "Passwords do not match!";
        }

        // 2. Kiểm tra xem Email (Username) này đã được đăng ký trong DB chưa
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            return "Account already exists!"; // Trả về lỗi nếu trùng khớp
        }

        // 3. Nếu mọi điều kiện đều đúng, khởi tạo thực thể và lưu
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(request.getPassword()); // Tạm thời lưu text thuần theo cấu trúc cũ của login
        
        // Mặc định role nếu client truyền null hoặc rỗng là CLIENT
        newUser.setRole((request.getRole() != null && !request.getRole().trim().isEmpty()) 
                        ? request.getRole() : "CLIENT");

        userRepository.save(newUser); // Khối "Save user data to database" trong Swimlane

        return "Registration Success!";
    }
}