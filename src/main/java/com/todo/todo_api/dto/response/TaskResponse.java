package com.todo.todo_api.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.todo.todo_api.domain.Priority;
import com.todo.todo_api.domain.Status;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResponse {
    private UUID id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
