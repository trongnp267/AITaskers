package com.example.demo.controller;

import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        String result = userService.checkLogin(username, password);

        if (result.contains("Success")) {
            // Tạo JWT Token sau khi login thành công
            String token = jwtTokenProvider.generateToken(username);
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", result,
                "token", token
            ));
        } else {
            return ResponseEntity.status(401).body(Map.of("status", "error", "message", result));
        }
    }
}