package com.todo.todo_api.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.annotation.JsonInclude;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    record ErrorResponse(String code, String message, List<FieldErrorDetail> details) {}

    record FieldErrorDetail(String field, String message) {}

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        ErrorResponse error = new ErrorResponse("NÃO_ENCONTRADO", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidStatus(InvalidStatusTransitionException ex) {
        ErrorResponse error = new ErrorResponse("TRANSIÇÃO_DE_STATUS_INVÁLIDA", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        ErrorResponse error = new ErrorResponse("ERRO_INTERNO", "Ocorreu um erro inesperado.", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
