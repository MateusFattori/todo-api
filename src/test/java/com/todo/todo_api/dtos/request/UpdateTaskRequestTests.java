package com.todo.todo_api.dtos.request;

import org.junit.jupiter.api.Test;

import com.todo.todo_api.dto.request.UpdateTaskRequest;

import jakarta.validation.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UpdateTaskRequestTests {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldAllowNullFields() {
        UpdateTaskRequest request = new UpdateTaskRequest();
        Set<ConstraintViolation<UpdateTaskRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void titleTooShort_shouldReturnViolation() {
        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTitle("ab");

        Set<ConstraintViolation<UpdateTaskRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("Title deve ter entre 3 e 255 caracteres", violations.iterator().next().getMessage());
    }
}