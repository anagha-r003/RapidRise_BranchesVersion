package com.rapidrise.task2_jwt_crud_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "Password must be strong"
    )
    private String newPassword;

    @NotBlank
    private String confirmPassword;
}
