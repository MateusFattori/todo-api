package com.todo.todo_api.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.UUID;

import com.todo.todo_api.domain.Priority;
import com.todo.todo_api.domain.Status;
import com.todo.todo_api.domain.Task;
import com.todo.todo_api.dto.request.CreateTaskRequest;
import com.todo.todo_api.dto.request.UpdateTaskRequest;
import com.todo.todo_api.dto.response.TaskResponse;

import org.junit.jupiter.api.Test;

class TaskMapperTests {

    @Test
    void toEntity_shouldMapAllFields() {
        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("Test Task")
                .description("Description")
                .status(Status.in_progress)
                .priority(Priority.high)
                .dueDate(LocalDateTime.now().plusDays(1))
                .build();

        Task task = TaskMapper.toEntity(request);

        assertEquals(request.getTitle(), task.getTitle());
        assertEquals(request.getDescription(), task.getDescription());
        assertEquals(request.getStatus(), task.getStatus());
        assertEquals(request.getPriority(), task.getPriority());
        assertEquals(request.getDueDate(), task.getDueDate());
    }

    @Test
    void toEntity_shouldSetDefaultStatusAndPriority() {
        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("Test Task")
                .description("Description")
                .build();

        Task task = TaskMapper.toEntity(request);

        assertEquals(Status.pending, task.getStatus());
        assertEquals(Priority.medium, task.getPriority());
    }

    @Test
    void toResponse_shouldMapAllFields() {
        Task task = Task.builder()
                .id(UUID.randomUUID())
                .title("Title")
                .description("Desc")
                .status(Status.done)
                .priority(Priority.low)
                .dueDate(LocalDateTime.now().plusDays(1))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        TaskResponse response = TaskMapper.toResponse(task);

        // Corrigido para usar getters
        assertEquals(task.getId(), response.getId());
        assertEquals(task.getTitle(), response.getTitle());
        assertEquals(task.getDescription(), response.getDescription());
        assertEquals(task.getStatus(), response.getStatus());
        assertEquals(task.getPriority(), response.getPriority());
        assertEquals(task.getDueDate(), response.getDueDate());
        assertEquals(task.getCreatedAt(), response.getCreatedAt());
        assertEquals(task.getUpdatedAt(), response.getUpdatedAt());
    }

    @Test
    void updateEntity_shouldUpdateOnlyNonNullFields() {
        Task task = Task.builder()
                .title("Old")
                .description("Old Desc")
                .status(Status.pending)
                .priority(Priority.medium)
                .build();

        UpdateTaskRequest request = UpdateTaskRequest.builder()
                .title("New")
                .status(Status.in_progress)
                .build();

        TaskMapper.updateEntity(task, request);

        assertEquals("New", task.getTitle());
        assertEquals(Status.in_progress, task.getStatus());
        assertEquals("Old Desc", task.getDescription());
        assertEquals(Priority.medium, task.getPriority());
        assertNotNull(task.getUpdatedAt());
    }
}