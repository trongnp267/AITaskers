package com.project.exception;

public class PaymentException extends BaseException {
    public PaymentException(int statusCode, String message) {
        super(statusCode, message);
    }
}
