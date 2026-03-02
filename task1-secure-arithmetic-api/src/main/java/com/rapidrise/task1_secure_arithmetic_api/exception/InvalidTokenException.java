package com.rapidrise.task1_secure_arithmetic_api.exception;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(String message) {
        super(message);
    }
}
