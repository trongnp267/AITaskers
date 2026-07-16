package com.aitasker.backend.controller;

import com.aitasker.backend.entity.User;
import com.aitasker.backend.repository.ClientProfileRepository;
import com.aitasker.backend.repository.ExpertProfileRepository;
import com.aitasker.backend.service.JwtService;
import com.aitasker.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ClientProfileRepository clientProfileRepository;

    @Autowired
    private ExpertProfileRepository expertProfileRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        User user = userService.authenticate(username, password);
        String token = jwtService.generateToken(user);

        Long profileId = null;
        if ("CLIENT".equalsIgnoreCase(user.getRole())) {
            profileId = clientProfileRepository.findByUser_Id(user.getId())
                    .map(p -> p.getClientId()).orElse(null);
        } else if ("EXPERT".equalsIgnoreCase(user.getRole())) {
            profileId = expertProfileRepository.findByUser_Id(user.getId())
                    .map(p -> p.getExpertId()).orElse(null);
        }

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("username", user.getUsername());
        userMap.put("role", user.getRole());
        userMap.put("profileId", profileId);

        Map<String, Object> body = new HashMap<>();
        body.put("status", "success");
        body.put("message", "Đăng nhập thành công");
        body.put("token", token);
        body.put("user", userMap);

        return ResponseEntity.ok(body);
    }
}
