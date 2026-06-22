package com.project.exception;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException(Long userId) {
        super("User not found: " + userId);
    }
}
