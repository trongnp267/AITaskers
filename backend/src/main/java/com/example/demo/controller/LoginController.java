package com.example.demo.controller;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private UserService userService;

    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        if (username == null || !EMAIL_PATTERN.matcher(username).matches()) {
            return ResponseEntity.status(400).body("Username must be a valid email address!");
        }

        String result = userService.checkLogin(username, password);

        if (result.contains("Success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(401).body(result);
        }
    }

    // --- THÊM ENDPOINT TIẾP NHẬN ĐĂNG KÝ VÀO ĐÂY ---
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerData) {
        String username = registerData.getUsername();

        // 1. Chặn nhanh định dạng Email ngay tại tầng Controller đầu vào
        if (username == null || !EMAIL_PATTERN.matcher(username).matches()) {
            return ResponseEntity.status(400).body("Username must be a valid email address!");
        }

        // 2. Gọi tầng Service xử lý các nghiệp vụ kiểm tra bên trong
        String result = userService.registerUser(registerData);

        // 3. Phân tích kết quả trả về để map thành mã HTTP Status thích hợp
        if (result.equals("Registration Success!")) {
            return ResponseEntity.ok(result); // Trả về 200 OK
        } else if (result.equals("Passwords do not match!")) {
            return ResponseEntity.status(400).body(result); // Trả về 400 Bad Request
        } else if (result.equals("Account already exists!")) {
            return ResponseEntity.status(409).body(result); // Trả về 409 Conflict (Trùng tài khoản)
        } else {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}