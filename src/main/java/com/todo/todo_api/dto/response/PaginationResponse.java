package com.todo.todo_api.dto.response;

public record PaginationResponse (
    int page, 
    int limit,
    long total_items,
    int total_pages
) {}
