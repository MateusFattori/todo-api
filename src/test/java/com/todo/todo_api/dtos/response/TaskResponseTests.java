package com.todo.todo_api.dtos.response;

import org.junit.jupiter.api.Test;

import com.todo.todo_api.dto.response.TaskResponse;

import java.util.UUID;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskResponseTests {

    @Test
    void builder_shouldCreateInstance() {
        TaskResponse response = TaskResponse.builder()
                .id(UUID.randomUUID())
                .title("Task")
                .description("Desc")
                .status(null)
                .priority(null)
                .dueDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assertEquals("Task", response.getTitle());
        assertNotNull(response.getCreatedAt());
    }
}