package com.rapidrise.task2_jwt_crud_api.controller;

import com.rapidrise.task2_jwt_crud_api.dto.ResponseStructure;
import com.rapidrise.task2_jwt_crud_api.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseStructure<String>> upload(
            @RequestParam("file") MultipartFile file)
            throws IOException {

        return fileService.uploadFile(file);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> download(@PathVariable Long id){

        return fileService.downloadFile(id);
    }
}
