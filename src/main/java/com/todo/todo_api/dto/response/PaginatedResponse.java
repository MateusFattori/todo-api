package com.todo.todo_api.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;

public record PaginatedResponse<T>(
    List<T> data,
    PaginationResponse pagination
) {
    
    public static <T> PaginatedResponse<T> from(Page<T> page) {
        return new PaginatedResponse<>(
            page.getContent(),
            new PaginationResponse(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
            )
        );
    } 
}
