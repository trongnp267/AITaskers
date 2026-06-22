package com.project.repository;

import com.project.model.User;

public interface IUserRepository {
    User findById(String userId);
}
