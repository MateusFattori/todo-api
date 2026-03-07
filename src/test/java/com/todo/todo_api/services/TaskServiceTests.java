package com.todo.todo_api.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.todo.todo_api.domain.*;
import com.todo.todo_api.dto.request.*;
import com.todo.todo_api.dto.response.*;
import com.todo.todo_api.exception.*;
import com.todo.todo_api.repository.TaskRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;

    private Task task;
    private UUID taskId;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        taskId = UUID.randomUUID();
        task = Task.builder()
                .id(taskId)
                .title("Test Task")
                .description("Desc")
                .status(Status.pending)
                .priority(Priority.medium)
                .build();
    }

    @Test
    void createTask_ShouldReturnTaskResponse() {
        CreateTaskRequest req = CreateTaskRequest.builder().title("Test Task").build();
        when(repository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = service.createTask(req);

        assertEquals("Test Task", response.getTitle());
    }

    @Test
    void getTaskById_NotFound_ShouldThrow() {
        when(repository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getTaskById(taskId));
    }

    @Test
    void updateTask_InvalidStatus_ShouldThrow() {
        UpdateTaskRequest req = UpdateTaskRequest.builder().status(Status.done).build();
        when(repository.findById(taskId)).thenReturn(Optional.of(task));

        assertThrows(InvalidStatusTransitionException.class, () -> service.updateTask(taskId, req));
    }

    @Test
    void deleteTask_NotFound_ShouldThrow() {
        when(repository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.deleteTask(taskId));
    }

    @Test
    void getTasks_InvalidPage_ShouldThrow() {
        assertThrows(InvalidPaginationException.class, () -> service.getTasks(null, null, "createdAt", "asc", 0, 20));
    }
}