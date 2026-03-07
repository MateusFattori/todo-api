package com.todo.todo_api.controller;

import com.todo.todo_api.dto.request.CreateTaskRequest;
import com.todo.todo_api.dto.request.UpdateTaskRequest;
import com.todo.todo_api.dto.response.TaskResponse;
import com.todo.todo_api.controler.TaskController;
import com.todo.todo_api.domain.Priority;
import com.todo.todo_api.domain.Status;
import com.todo.todo_api.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private TaskService taskService;
    private TaskController taskController;

    private UUID taskId;
    private TaskResponse taskResponse;

    @BeforeEach
    void setup() {
        taskService = Mockito.mock(TaskService.class);
        taskController = new TaskController(taskService);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

        objectMapper = new ObjectMapper();
        taskId = UUID.randomUUID();
        taskResponse = TaskResponse.builder()
                .id(taskId)
                .title("Test Task")
                .status(Status.pending)
                .priority(Priority.medium)
                .build();
    }

    @Test
    void createTask_ShouldReturn201() throws Exception {
        CreateTaskRequest request = CreateTaskRequest.builder().title("Test Task").build();
        when(taskService.createTask(any(CreateTaskRequest.class))).thenReturn(taskResponse);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value("Test Task"))
                .andExpect(jsonPath("$.data.id").value(taskId.toString()));
    }

    @Test
    void getTaskById_ShouldReturn200() throws Exception {
        when(taskService.getTaskById(taskId)).thenReturn(taskResponse);

        mockMvc.perform(get("/api/todos/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(taskId.toString()))
                .andExpect(jsonPath("$.data.title").value("Test Task"));
    }

    @Test
    void deleteTask_ShouldReturn200() throws Exception {
        doNothing().when(taskService).deleteTask(taskId);

        mockMvc.perform(delete("/api/todos/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Task deleted successfully"));
    }

    @Test
    void updateTask_ShouldReturn200() throws Exception {
        UpdateTaskRequest request = UpdateTaskRequest.builder().title("Updated Task").build();
        TaskResponse updatedTask = TaskResponse.builder()
                .id(taskId)
                .title("Updated Task")
                .status(Status.pending)
                .priority(Priority.medium)
                .build();

        when(taskService.updateTask(taskId, request)).thenReturn(updatedTask);

        mockMvc.perform(put("/api/todos/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Updated Task"))
                .andExpect(jsonPath("$.data.id").value(taskId.toString()));
    }
}