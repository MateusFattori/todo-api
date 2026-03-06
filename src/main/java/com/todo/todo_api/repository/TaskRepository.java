package com.todo.todo_api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todo.todo_api.domain.Task;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    
}
