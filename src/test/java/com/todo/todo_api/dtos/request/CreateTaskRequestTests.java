package com.todo.todo_api.dtos.request;

import org.junit.jupiter.api.Test;

import com.todo.todo_api.dto.request.CreateTaskRequest;

import jakarta.validation.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CreateTaskRequestTests {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldValidateCorrectRequest() {
        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("Minha Tarefa")
                .description("Descrição da tarefa")
                .build();

        Set<ConstraintViolation<CreateTaskRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void titleBlank_shouldReturnViolation() {
        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("")
                .build();

        Set<ConstraintViolation<CreateTaskRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("Title é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    void titleTooShort_shouldReturnViolation() {
        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("ab")
                .build();

        Set<ConstraintViolation<CreateTaskRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("Title deve ter entre 3 e 255 caracteres", violations.iterator().next().getMessage());
    }
}