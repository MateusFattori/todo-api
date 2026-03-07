package com.todo.todo_api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandllerTests {

    private GlobalExceptionHandller handler;

    @BeforeEach
    void setup() {
        handler = new GlobalExceptionHandller();
    }

    @Test
    void handleNotFound_shouldReturn404() {
        NotFoundException ex = new NotFoundException("Item não encontrado");
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        ResponseEntity<?> response = handler.handleNotFound(ex, request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void handleInvalidStatus_shouldReturn400() {
        InvalidStatusTransitionException ex = new InvalidStatusTransitionException(null, null);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        ResponseEntity<?> response = handler.handleInvalidStatus(ex, request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void handleInvalidPagination_shouldReturn400() {
        InvalidPaginationException ex = new InvalidPaginationException("Página inválida");
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        ResponseEntity<?> response = handler.handleInvalidPagination(ex, request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void handleGeneric_shouldReturn500() {
        Exception ex = new RuntimeException("Erro genérico");
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        ResponseEntity<?> response = handler.handleGeneric(ex, request);

        assertNotNull(response);
        assertEquals(500, response.getStatusCode().value());
    }
}