package com.todo.todo_api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.todo.todo_api.domain.Priority;
import com.todo.todo_api.domain.Status;
import com.todo.todo_api.domain.Task;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    Page<Task> findAllByStatus(Status status, Pageable pageable);
    Page<Task> findAllByPriority(Priority priority, Pageable pageable);
    Page<Task> findAllByStatusAndPriority(Status status, Priority priority, Pageable pageable);
    List<Task> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
}
