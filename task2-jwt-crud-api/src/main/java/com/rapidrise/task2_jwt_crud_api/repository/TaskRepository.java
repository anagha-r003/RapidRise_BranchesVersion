package com.rapidrise.task2_jwt_crud_api.repository;

import com.rapidrise.task2_jwt_crud_api.entity.Task;
import com.rapidrise.task2_jwt_crud_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findByUser(User user);
}
