package com.todo.todo_api.controler;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.todo.todo_api.domain.Priority;
import com.todo.todo_api.domain.Status;
import com.todo.todo_api.dto.request.CreateTaskRequest;
import com.todo.todo_api.dto.request.UpdateTaskRequest;
import com.todo.todo_api.dto.response.ApiResponse;
import com.todo.todo_api.dto.response.PaginatedResponse;
import com.todo.todo_api.dto.response.TaskResponse;
import com.todo.todo_api.services.TaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/todos")
@RequiredArgsConstructor
@Validated
public class TaskController {
    
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(@Valid @RequestBody CreateTaskRequest request) {
        TaskResponse task = taskService.createTask(request);
        ApiResponse<TaskResponse> response = new ApiResponse<>(task, "Task created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(@PathVariable UUID id) {
        TaskResponse task = taskService.getTaskById(id);
        ApiResponse<TaskResponse> response = new ApiResponse<>(task, "Task retrieved successfully");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(@PathVariable UUID id, 
        @Valid @RequestBody UpdateTaskRequest request) {
        TaskResponse task = taskService.updateTask(id, request);
        ApiResponse<TaskResponse> response = new ApiResponse<>(task, "Task updated successfully");
        return ResponseEntity.ok(response);
    }

        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable UUID id) {

            taskService.deleteTask(id);

            ApiResponse<Void> response =
                    new ApiResponse<>(null, "Task deleted successfully");

            return ResponseEntity.ok(response);
        }

    @GetMapping
    public ResponseEntity<PaginatedResponse<TaskResponse>> getAllTasks(
        @RequestParam(required = false) Status status,
        @RequestParam(required = false) Priority priority,
        @RequestParam(defaultValue = "createdAt") String sort,
        @RequestParam(defaultValue = "asc") String order,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "20") int limit
    ) {
        Page<TaskResponse> taskPage = taskService.getTasks(status, priority, sort, order, page, limit);

        return ResponseEntity.ok(PaginatedResponse.from(taskPage));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> searchTasks(@RequestParam("q") String query) {
        List<TaskResponse> tasks = taskService.searchTasks(query);
        ApiResponse<List<TaskResponse>> apiResponse = new ApiResponse<>(tasks, "Tasks retrieved successfully");
        return ResponseEntity.ok(apiResponse);
    }
}
