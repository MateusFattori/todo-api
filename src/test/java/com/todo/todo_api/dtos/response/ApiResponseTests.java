package com.todo.todo_api.dtos.response;

import org.junit.jupiter.api.Test;

import com.todo.todo_api.dto.response.ApiResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTests {

    @Test
    void record_shouldStoreDataAndMessage() {
        ApiResponse<String> response = new ApiResponse<>("ok", "Success");
        assertEquals("ok", response.data());
        assertEquals("Success", response.message());
    }

    @Test
    void record_withList() {
        ApiResponse<List<String>> response = new ApiResponse<>(List.of("A", "B"), "Success");
        assertEquals(2, response.data().size());
        assertEquals("Success", response.message());
    }
}