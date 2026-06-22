package com.project.exception;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String message) {
        super(404, message);
    }
}
