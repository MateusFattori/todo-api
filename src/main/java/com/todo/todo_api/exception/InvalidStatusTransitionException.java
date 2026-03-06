package com.todo.todo_api.exception;

import com.todo.todo_api.domain.Status;

public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(Status from, Status to) {
        super(String.format("Transição de status inválida de %s para %s", from, to));
    }
    
}
