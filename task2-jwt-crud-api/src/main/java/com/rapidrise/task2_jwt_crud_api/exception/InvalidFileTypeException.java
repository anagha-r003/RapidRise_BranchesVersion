package com.rapidrise.task2_jwt_crud_api.exception;

public class InvalidFileTypeException extends RuntimeException{
    public InvalidFileTypeException(String message){
        super(message);
    }
}
