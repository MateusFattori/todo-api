package com.todo.todo_api.exception;

import org.junit.jupiter.api.Test;
import com.todo.todo_api.domain.Status;

import static org.junit.jupiter.api.Assertions.*;

class InvalidStatusTransitionExceptionTests {

    @Test
    void constructor_shouldFormatMessage() {
        InvalidStatusTransitionException ex =
                new InvalidStatusTransitionException(Status.pending, Status.done);

        assertEquals("Transição de status inválida de pending para done", ex.getMessage());
    }
}