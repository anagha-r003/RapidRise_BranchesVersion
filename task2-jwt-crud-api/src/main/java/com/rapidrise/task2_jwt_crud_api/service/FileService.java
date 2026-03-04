package com.rapidrise.task2_jwt_crud_api.service;

import com.rapidrise.task2_jwt_crud_api.dto.ResponseStructure;
import com.rapidrise.task2_jwt_crud_api.entity.FileEntity;
import com.rapidrise.task2_jwt_crud_api.entity.User;
import com.rapidrise.task2_jwt_crud_api.exception.FileNotFoundException;
import com.rapidrise.task2_jwt_crud_api.exception.InvalidFileTypeException;
import com.rapidrise.task2_jwt_crud_api.exception.UserNotFoundException;
import com.rapidrise.task2_jwt_crud_api.repository.FileRepository;
import com.rapidrise.task2_jwt_crud_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final List<String> allowedTypes =
            Arrays.asList("image/png","image/jpeg","image/webp","application/pdf");


    private User getLoggedInUser(){

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }


    public ResponseEntity<ResponseStructure<String>> uploadFile(MultipartFile file)
            throws IOException {

        if(file.isEmpty()){
            throw new InvalidFileTypeException("File is empty");
        }

        if(!allowedTypes.contains(file.getContentType())){
            throw new InvalidFileTypeException("Invalid file type");
        }

        User user = getLoggedInUser();

        String projectPath = System.getProperty("user.dir");

        File folder = new File(projectPath + File.separator + uploadDir);

        if(!folder.exists()){
            folder.mkdirs();
        }

        String originalName = file.getOriginalFilename().replaceAll("\\s+","_");

        String uniqueName = UUID.randomUUID() + "_" + originalName;

        String filePath = folder.getAbsolutePath() + File.separator + uniqueName;

        file.transferTo(new File(filePath));

        FileEntity entity = new FileEntity();

        entity.setFileName(uniqueName);
        entity.setFileType(file.getContentType());
        entity.setFilePath(filePath);
        entity.setUser(user);

        fileRepository.save(entity);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseStructure<>(201,"File Uploaded",uniqueName));
    }


    public ResponseEntity<Resource> downloadFile(Long id){

        User user = getLoggedInUser();

        FileEntity entity = fileRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("File not found"));

        if(!entity.getUser().getId().equals(user.getId())){
            throw new FileNotFoundException("Unauthorized access");
        }

        File file = new File(entity.getFilePath());

        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header("Content-Type", entity.getFileType())
                .header("Content-Disposition",
                        "attachment; filename=\"" + entity.getFileName() + "\"")
                .body(resource);
    }

}
