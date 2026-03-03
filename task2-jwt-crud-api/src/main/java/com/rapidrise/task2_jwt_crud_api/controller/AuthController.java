package com.rapidrise.task2_jwt_crud_api.controller;

import com.rapidrise.task2_jwt_crud_api.dto.ForgotPasswordRequest;
import com.rapidrise.task2_jwt_crud_api.dto.LoginRequest;
import com.rapidrise.task2_jwt_crud_api.dto.RegisterRequest;
import com.rapidrise.task2_jwt_crud_api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request){
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        return authService.login(request);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request){
        return authService.forgotPassword(request);
    }
}
