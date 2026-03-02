package com.rapidrise.task1_secure_arithmetic_api.exception;

public class DivisionByZeroException extends RuntimeException{
    public DivisionByZeroException(String message) {
        super(message);
    }
}
