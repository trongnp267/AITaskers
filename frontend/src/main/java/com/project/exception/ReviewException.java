package com.project.exception;

public class ReviewException extends BaseException {
    public ReviewException(int statusCode, String message) {
        super(statusCode, message);
    }
}
