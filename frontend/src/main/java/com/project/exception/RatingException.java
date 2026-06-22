package com.project.exception;

public class RatingException extends BaseException {
    public RatingException(int statusCode, String message) {
        super(statusCode, message);
    }
}
