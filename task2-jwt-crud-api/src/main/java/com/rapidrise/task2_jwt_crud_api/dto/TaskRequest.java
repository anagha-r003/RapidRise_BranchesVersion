package com.rapidrise.task2_jwt_crud_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskRequest {
    @NotBlank
    private String title;

    private String description;
}
