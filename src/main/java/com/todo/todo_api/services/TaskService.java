package com.todo.todo_api.services;

import java.util.List;
import java.util.UUID;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        .orElseThrow(() -> new NotFoundException("Task com o id '" + id + "' não encontrado."));
        return TaskMapper.toResponse(task);
    }

    @Transactional
    public TaskResponse updateTask(UUID id, UpdateTaskRequest request) {
        Task task = repository.findById(id)
        .orElseThrow(() -> new NotFoundException("Task com o id '" + id + "' não encontrado."));

        if (request.getStatus() != null && !isValidStatusTransition(task.getStatus(), request.getStatus())) {
            throw new InvalidStatusTransitionException(task.getStatus(), request.getStatus());
        }

        TaskMapper.updateEntity(task, request);

        Task update = repository.save(task);
        return TaskMapper.toResponse(update);
    }

    @Transactional
    public void deleteTask(UUID id) {
        Task task = repository.findById(id)
        .orElseThrow(() -> new NotFoundException("Task com o id '" + id + "' não encontrado."));
        repository.delete(task);
    }

    private boolean isValidStatusTransition(Status current, Status next) {
        return switch (current) {
            case PENDING -> next == Status.IN_PROGRESS || next == Status.PENDING;
            case IN_PROGRESS -> next == Status.DONE || next == Status.PENDING || next == Status.IN_PROGRESS;
            case DONE -> next == Status.PENDING || next == Status.DONE;
        };
    }
}
