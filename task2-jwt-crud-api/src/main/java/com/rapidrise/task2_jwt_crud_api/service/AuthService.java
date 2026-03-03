package com.rapidrise.task2_jwt_crud_api.service;

import com.rapidrise.task2_jwt_crud_api.dto.*;
import com.rapidrise.task2_jwt_crud_api.entity.RefreshToken;
import com.rapidrise.task2_jwt_crud_api.entity.User;
import com.rapidrise.task2_jwt_crud_api.exception.InvalidCredentialsException;
import com.rapidrise.task2_jwt_crud_api.exception.UserAlreadyExistsException;
import com.rapidrise.task2_jwt_crud_api.repository.RefreshTokenRepository;
import com.rapidrise.task2_jwt_crud_api.repository.UserRepository;
import com.rapidrise.task2_jwt_crud_api.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public ResponseEntity<ResponseStructure<AuthResponse>> register(RegisterRequest request){

        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new UserAlreadyExistsException("Username already exists");
        }

        if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new RuntimeException("Passwords do not match");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseStructure<>(201,"User Registered",null));
    }

    public ResponseEntity<ResponseStructure<AuthResponse>> login(LoginRequest request){

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException("Invalid password");
        }

        //delete old refresh token
        refreshTokenRepository.deleteByUser(user);

        String accessToken = jwtUtil.generateAccessToken(user.getUsername());

        String refreshTokenValue = UUID.randomUUID().toString();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenValue);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(604800000));

        refreshTokenRepository.save(refreshToken);

        AuthResponse response = new AuthResponse(accessToken, refreshTokenValue);

        return ResponseEntity.ok(
                new ResponseStructure<>(200,"Login Successful",response)
        );
    }

    public ResponseEntity<ResponseStructure<String>> forgotPassword(ForgotPasswordRequest request){

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!request.getNewPassword().equals(request.getConfirmPassword())){
            throw new RuntimeException("Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(
                new ResponseStructure<>(200,"Password updated successfully",null)
        );
    }

    public ResponseEntity<ResponseStructure<AuthResponse>> refreshToken(RefreshTokenRequest request){

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if(refreshToken.getExpiryDate().isBefore(Instant.now())){
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired. Please login again.");
        }

        String newAccessToken =
                jwtUtil.generateAccessToken(refreshToken.getUser().getUsername());

        AuthResponse response = new AuthResponse(
                newAccessToken,
                refreshToken.getToken()
        );

        return ResponseEntity.ok(
                new ResponseStructure<>(200,"Access token refreshed",response)
        );
    }
}
