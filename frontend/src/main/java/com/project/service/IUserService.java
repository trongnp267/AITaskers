package com.project.service;

import com.project.model.User;

public interface IUserService {
    User findById(String userId);
}
