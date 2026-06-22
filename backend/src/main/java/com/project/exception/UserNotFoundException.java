package com.project.exception;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException(Object userId) {
        super("User not found: " + userId);
    }
}
