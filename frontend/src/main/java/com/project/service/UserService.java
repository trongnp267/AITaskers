package com.project.service;

import com.project.exception.UserNotFoundException;
import com.project.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
    @Override
    public User findById(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new UserNotFoundException("Khong tim thay user.");
        }

        return new User(userId, "");
    }
}
