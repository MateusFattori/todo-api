package com.todo.todo_api.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionTests {

    @Test
    void constructor_shouldSetMessage() {
        NotFoundException ex = new NotFoundException("Not found");
        assertEquals("Not found", ex.getMessage());
    }
}