package com.todo.todo_api.dto.response;

public record ApiResponse<T>(
    T data, 
    String message
) {}
