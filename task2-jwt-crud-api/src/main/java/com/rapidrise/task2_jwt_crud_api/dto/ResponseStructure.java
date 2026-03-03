package com.rapidrise.task2_jwt_crud_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseStructure<T> {
    private int status;
    private String message;
    private T data;
}
