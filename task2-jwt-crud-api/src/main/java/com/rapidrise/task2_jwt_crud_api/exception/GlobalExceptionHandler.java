package com.rapidrise.task2_jwt_crud_api.exception;

import com.rapidrise.task2_jwt_crud_api.dto.ResponseStructure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ResponseStructure<String>> handleUserExists(UserAlreadyExistsException ex){

        ResponseStructure<String> structure =
                new ResponseStructure<>(409, ex.getMessage(), null);

        return new ResponseEntity<>(structure, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ResponseStructure<String>> handleInvalidCredentials(InvalidCredentialsException ex){

        ResponseStructure<String> structure =
                new ResponseStructure<>(401, ex.getMessage(), null);

        return new ResponseEntity<>(structure, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseStructure<String>> handleUserNotFound(UserNotFoundException ex){

        ResponseStructure<String> structure =
                new ResponseStructure<>(404, ex.getMessage(), null);

        return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseStructure<String>> handleGeneric(Exception ex){

        ResponseStructure<String> structure =
                new ResponseStructure<>(500, "Internal Server Error", null);

        return new ResponseEntity<>(structure, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseStructure<Map<String, String>>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ResponseStructure<Map<String, String>> structure =
                new ResponseStructure<>(400, "Validation Failed", errors);

        return new ResponseEntity<>(structure, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ResponseStructure<String>> handleFileNotFound(FileNotFoundException ex){

        ResponseStructure<String> structure =
                new ResponseStructure<>(404, ex.getMessage(), null);

        return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<ResponseStructure<String>> handleInvalidFileType(InvalidFileTypeException ex){

        ResponseStructure<String> structure =
                new ResponseStructure<>(400, ex.getMessage(), null);

        return new ResponseEntity<>(structure, HttpStatus.BAD_REQUEST);
    }



}
