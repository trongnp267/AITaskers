package com.example.demo.controller;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RegisterController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String result = userService.registerUser(request);
        
        if (result.contains("Success")) {
            return ResponseEntity.ok(Map.of("status", "success", "message", result));
        } else {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", result));
        }
    }
}