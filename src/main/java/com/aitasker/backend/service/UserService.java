package com.aitasker.backend.service;

import com.aitasker.backend.dto.RegisterRequest;
import com.aitasker.backend.entity.*;
import com.aitasker.backend.exception.BadRequestException;
import com.aitasker.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired private UserRepository userRepository;
    @Autowired private ClientProfileRepository clientProfileRepository;
    @Autowired private ExpertProfileRepository expertProfileRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException(
                        "Tên đăng nhập không tồn tại. Hãy dùng đúng email bạn đã nhập khi đăng ký."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("Mật khẩu không đúng.");
        }

        if (user.getStatus() == AccountStatus.PENDING) {
            throw new BadRequestException(
                    "Tài khoản của bạn đang chờ admin phê duyệt. Vui lòng đợi hoặc liên hệ quản trị viên.");
        }
        if (user.getStatus() == AccountStatus.REJECTED) {
            throw new BadRequestException("Tài khoản của bạn đã bị từ chối.");
        }

        return user;
    }

    @Transactional
    public String registerUser(RegisterRequest request) {
        if (request.getPassword() == null || !request.getPassword().equals(request.getConfirmPassword())) {
            return "Passwords do not match!";
        }

        if (request.getRole() == null || request.getRole().isBlank()) {
            return "Role is required!";
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return "Account already exists!";
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(hashedPassword);
        newUser.setRole(request.getRole().toUpperCase());
        newUser.setStatus(AccountStatus.PENDING);
        User savedUser = userRepository.save(newUser);

        Wallet wallet = new Wallet();
        wallet.setUser(savedUser);
        wallet.setBalance(BigDecimal.ZERO);
        walletRepository.save(wallet);

        if ("CLIENT".equals(savedUser.getRole())) {
            ClientProfile client = new ClientProfile();
            client.setCompanyName(request.getCompanyName());
            client.setDescription(request.getDescription());
            client.setUser(savedUser);
            client.setCreatedAt(LocalDateTime.now());
            clientProfileRepository.save(client);
        } else if ("EXPERT".equals(savedUser.getRole())) {
            ExpertProfile expert = new ExpertProfile();
            expert.setDescription(request.getDescription());
            expert.setExperienceYears(0);
            expert.setRating(BigDecimal.valueOf(5.0));
            expert.setCompletedJobs(0);
            expert.setUser(savedUser);
            expertProfileRepository.save(expert);
        }

        return "Registration Success!";
    }
}
