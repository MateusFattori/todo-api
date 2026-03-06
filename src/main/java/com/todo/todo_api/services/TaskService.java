package com.todo.todo_api.services;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.todo.todo_api.domain.Priority;
import com.todo.todo_api.domain.Status;
import com.todo.todo_api.domain.Task;
import com.todo.todo_api.dto.request.CreateTaskRequest;
import com.todo.todo_api.dto.request.UpdateTaskRequest;
import com.todo.todo_api.dto.response.TaskResponse;
import com.todo.todo_api.exception.InvalidStatusTransitionException;
import com.todo.todo_api.exception.NotFoundException;
import com.todo.todo_api.mapper.TaskMapper;
import com.todo.todo_api.repository.TaskRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
    
    private final TaskRepository repository;

    @Transactional
    public TaskResponse createTask(CreateTaskRequest request) {

        validateDueDate(request.getDueDate());

        Task task = TaskMapper.toEntity(request);
        task = repository.save(task);

        return TaskMapper.toResponse(task);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        List<Task> tasks = repository.findAll();
        return tasks.stream().map(TaskMapper::toResponse)
        .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(UUID id) {
        Task task = repository.findById(id)
        .orElseThrow(() -> new NotFoundException("Task with id '" + id + "'not found."));
        return TaskMapper.toResponse(task);
    }

    @Transactional
    public TaskResponse updateTask(UUID id, UpdateTaskRequest request) {

        Task task = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with id '" + id + "' not found."));

        if (request.getDueDate() != null) {
            validateDueDate(request.getDueDate());
        }

        if (request.getStatus() != null &&
                !isValidStatusTransition(task.getStatus(), request.getStatus())) {

            throw new InvalidStatusTransitionException(task.getStatus(), request.getStatus());
        }

        TaskMapper.updateEntity(task, request);

        Task updated = repository.save(task);

        return TaskMapper.toResponse(updated);
    }

    @Transactional
    public void deleteTask(UUID id) {
        Task task = repository.findById(id)
        .orElseThrow(() -> new NotFoundException("Task with id '" + id + "'not found."));
        repository.delete(task);
    }

    @Transactional(readOnly = true)
    public Page<TaskResponse> getTasks(
            Status status,
            Priority priority,
            String sort,
            String order,
            int page,
            int limit) {

        validatePagination(page, limit);

        Sort.Direction direction =
                "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;

        String sortField = validateSortField(sort);

        Pageable pageable =
                PageRequest.of(page - 1, Math.min(limit, 100), Sort.by(direction, sortField));

        Page<Task> taskPage;

        if (status != null && priority != null) {
            taskPage = repository.findAllByStatusAndPriority(status, priority, pageable);
        } else if (status != null) {
            taskPage = repository.findAllByStatus(status, pageable);
        } else if (priority != null) {
            taskPage = repository.findAllByPriority(priority, pageable);
        } else {
            taskPage = repository.findAll(pageable);
        }

        return taskPage.map(TaskMapper::toResponse);
    }


    public List<TaskResponse> searchTasks (String query) {
        List<Task> tasks = repository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
        return tasks.stream().map(TaskMapper::toResponse).toList();
    }

    private void validateDueDate(LocalDateTime dueDate) {

        if (dueDate != null &&
                dueDate.isBefore(LocalDateTime.now(ZoneOffset.UTC))) {

            throw new IllegalArgumentException("Due date must be in the future");
        }
    }

    private void validatePagination(int page, int limit) {

        if (page < 1) {
            throw new IllegalArgumentException("Page must be greater than 0");
        }

        if (limit < 1) {
            throw new IllegalArgumentException("Limit must be greater than 0");
        }
    }

    private String validateSortField(String sort) {

        if ("dueDate".equalsIgnoreCase(sort)) {
            return "dueDate";
        }

        return "createdAt";
    }

    private boolean isValidStatusTransition(Status current, Status next) {
        return switch (current) {
            case peding -> next == Status.in_preogress || next == Status.peding;
            case in_preogress -> next == Status.done || next == Status.peding || next == Status.in_preogress;
            case done -> next == Status.peding || next == Status.done;
        };
    }
}
