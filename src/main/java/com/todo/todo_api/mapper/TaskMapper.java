package com.todo.todo_api.mapper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.todo.todo_api.domain.Task;
import com.todo.todo_api.dto.request.CreateTaskRequest;
import com.todo.todo_api.dto.request.UpdateTaskRequest;
import com.todo.todo_api.dto.response.TaskResponse;
import com.todo.todo_api.domain.Status;
import com.todo.todo_api.domain.Priority;

public class TaskMapper {
    
    public static Task toEntity(CreateTaskRequest request) {
        return Task.builder()
        .title(request.getTitle())
        .description(request.getDescription())
        .status(request.getStatus() != null ? request.getStatus() : Status.PENDING)
        .priority(request.getPriority() != null ? request.getPriority() : Priority.MEDIUM)
        .dueDate(request.getDueDate())
        .build();
    }

    public static TaskResponse toResponse(Task task) {
        return TaskResponse.builder()   
            .id(task.getId())
            .title(task.getTitle())
            .description(task.getDescription())
            .status(task.getStatus())
            .priority(task.getPriority())
            .dueDate(task.getDueDate())
            .createdAt(task.getCreatedAt())
            .updatedAt(task.getUpdatedAt())
            .build();
    }

    public static void updateEntity(Task task, UpdateTaskRequest request) {
        if(request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if(request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if(request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        if(request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }
        if(request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }

        task.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
    }
}
