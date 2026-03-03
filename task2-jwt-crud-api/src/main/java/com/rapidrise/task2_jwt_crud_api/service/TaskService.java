package com.rapidrise.task2_jwt_crud_api.service;

import com.rapidrise.task2_jwt_crud_api.dto.ResponseStructure;
import com.rapidrise.task2_jwt_crud_api.dto.TaskRequest;
import com.rapidrise.task2_jwt_crud_api.entity.Task;
import com.rapidrise.task2_jwt_crud_api.entity.User;
import com.rapidrise.task2_jwt_crud_api.repository.TaskRepository;
import com.rapidrise.task2_jwt_crud_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private User getLoggedInUser() {
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // CREATE
    public ResponseEntity<ResponseStructure<Task>> createTask(TaskRequest request){

        User user = getLoggedInUser();

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setUser(user);

        taskRepository.save(task);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseStructure<>(201,"Task Created",task));
    }

    // READ ALL (for logged-in user only)
    public ResponseEntity<ResponseStructure<List<Task>>> getMyTasks(){

        User user = getLoggedInUser();

        List<Task> tasks = taskRepository.findByUser(user);

        return ResponseEntity.ok(
                new ResponseStructure<>(200,"Tasks Fetched",tasks)
        );
    }

    // UPDATE
    public ResponseEntity<ResponseStructure<Task>> updateTask(Long id, TaskRequest request){

        User user = getLoggedInUser();

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if(!task.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Unauthorized access");
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());

        taskRepository.save(task);

        return ResponseEntity.ok(
                new ResponseStructure<>(200,"Task Updated",task)
        );
    }

    // DELETE
    public ResponseEntity<ResponseStructure<String>> deleteTask(Long id){

        User user = getLoggedInUser();

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if(!task.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Unauthorized access");
        }

        taskRepository.delete(task);

        return ResponseEntity.ok(
                new ResponseStructure<>(200,"Task Deleted",null)
        );
    }
}
