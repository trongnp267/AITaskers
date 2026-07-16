package com.aitasker.backend.controller;

import com.aitasker.backend.entity.User;
import com.aitasker.backend.service.JwtService;
import com.aitasker.backend.service.UserService;
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
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        User user = userService.authenticate(username, password);
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "Login Successful",
            "token", token
        ));
    }
}
