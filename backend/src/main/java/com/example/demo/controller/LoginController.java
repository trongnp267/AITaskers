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

    // --- BỔ SUNG ENDPOINT ĐĂNG KÝ TÀI KHOẢN ---
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerData) {
        String username = registerData.getUsername();

        // 1. Kiểm tra định dạng Email ngay tại đầu vào
        if (username == null || !EMAIL_PATTERN.matcher(username).matches()) {
            return ResponseEntity.status(400).body("Username must be a valid email address!");
        }

        // 2. Gọi Service để xử lý nghiệp vụ đăng ký
        String result = userService.registerUser(registerData);

        // 3. Phản hồi HTTP Status Code dựa trên kết quả nghiệp vụ (Ứng với rẽ nhánh trong Swimlane)
        if (result.contains("Success")) {
            return ResponseEntity.ok(result); // 200 OK thành công
        } else if (result.equals("Account already exists!")) {
            return ResponseEntity.status(409).body(result); // 409 Conflict (Trùng tài khoản)
        } else {
            return ResponseEntity.status(400).body(result); // 400 Bad Request (Mật khẩu không khớp)
        }
    }
}