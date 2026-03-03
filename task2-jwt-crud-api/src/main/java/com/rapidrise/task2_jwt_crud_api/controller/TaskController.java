package com.rapidrise.task2_jwt_crud_api.controller;

import com.rapidrise.task2_jwt_crud_api.dto.TaskRequest;
import com.rapidrise.task2_jwt_crud_api.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskRequest request){
        return taskService.createTask(request);
    }

    @GetMapping
    public ResponseEntity<?> getMyTasks(){
        return taskService.getMyTasks();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request){
        return taskService.updateTask(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id){
        return taskService.deleteTask(id);
    }
}
