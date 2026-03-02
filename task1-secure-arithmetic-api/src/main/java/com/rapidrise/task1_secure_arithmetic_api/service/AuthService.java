package com.rapidrise.task1_secure_arithmetic_api.service;

import com.rapidrise.task1_secure_arithmetic_api.dto.AuthRequest;
import com.rapidrise.task1_secure_arithmetic_api.dto.AuthResponse;
import com.rapidrise.task1_secure_arithmetic_api.security.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JwtUtil jwtUtil;

    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse authenticate(AuthRequest request) {
        // Hardcoded username/password
        if ("admin".equals(request.getUsername()) && "admin123".equals(request.getPassword())) {
            String token = jwtUtil.generateToken(request.getUsername());
            return new AuthResponse(token);
        } else {
            throw new RuntimeException("Invalid Credentials");
        }
    }
}
