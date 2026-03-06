package com.todo.todo_api.services;

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
        .orElseThrow(() -> new NotFoundException("Task with id '" + id + "'not found."));

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
        .orElseThrow(() -> new NotFoundException("Task with id '" + id + "'not found."));
        repository.delete(task);
    }

    private boolean isValidStatusTransition(Status current, Status next) {
        return switch (current) {
            case PENDING -> next == Status.IN_PROGRESS || next == Status.PENDING;
            case IN_PROGRESS -> next == Status.DONE || next == Status.PENDING || next == Status.IN_PROGRESS;
            case DONE -> next == Status.PENDING || next == Status.DONE;
        };
    }

    public Page<TaskResponse> getTasks(Status status, Priority priority, String sort, String order, int page, int limit) {
        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortby = "dueDate".equalsIgnoreCase(sort) ? "dueDate"  : "createdAt";
        Pageable pageable = PageRequest.of(page - 1, Math.min(limit, 100), Sort.by(direction, sortby));

        Page<Task> taskPage;

        if(status != null && priority != null) {
            taskPage = repository.findAllByStatusAndPriority(status, priority, pageable);
        } else if (status != null){
            taskPage = repository.findAllByStatus(status, pageable);
        } else if (priority != null) {
            taskPage = repository.findAllByPriority(priority, pageable);
        } else {
            taskPage = repository.findAll(pageable);
        }
        return taskPage.map(TaskMapper::toResponse);
    }

    public List<TaskResponse> searchTasks (String query) {
        List<Task> taks = repository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
        return taks.stream().map(TaskMapper::toResponse).toList();
    }

}
