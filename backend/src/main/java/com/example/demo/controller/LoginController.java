package com.example.demo.controller;

import com.example.demo.dto.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        User user = userService.authenticate(username, password);

        if (user != null) {
            UserResponse userResponse = new UserResponse();
            userResponse.setId(user.getId());
            userResponse.setUsername(user.getUsername());
            userResponse.setRole(user.getRole());
            
            String token = jwtService.generateToken(user);
            
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Login Successful",
                "token", token,
                "user", userResponse
            ));
        } else {
            return ResponseEntity.status(401).body(Map.of(
                "status", "error", 
                "message", "Invalid username or password"
            ));
        }
    }
}