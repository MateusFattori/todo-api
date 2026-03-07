package com.todo.todo_api.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidPaginationExceptionTests {

    @Test
    void constructor_shouldSetMessage() {
        InvalidPaginationException ex = new InvalidPaginationException("Invalid page");
        assertEquals("Invalid page", ex.getMessage());
    }
}