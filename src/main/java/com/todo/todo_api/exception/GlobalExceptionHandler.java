package com.todo.todo_api.exception;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    
    public record ErrorResponse(String code, String message, List<FieldErrorDetail> details) {}
    public record FieldErrorDetail(String field, String message) {}

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        String requestId = getRequestId(request);
        logError(ex, request, requestId, HttpStatus.NOT_FOUND);
        ErrorResponse error = new ErrorResponse("NOT_FOUND", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidStatus(InvalidStatusTransitionException ex, HttpServletRequest request) {
        String requestId = getRequestId(request);
        logError(ex, request, requestId, HttpStatus.BAD_REQUEST);
        ErrorResponse error = new ErrorResponse("TRANSITION_INVALID", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        String requestId = getRequestId(request);
        logError(ex, request, requestId, HttpStatus.INTERNAL_SERVER_ERROR);
        ErrorResponse error = new ErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected error occurred.", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private String getRequestId(HttpServletRequest request) {
        Object requestId = request.getAttribute("X-Request-Id");
        return requestId != null ? requestId.toString() : "unknown";
    }

    private void logError(Exception ex, HttpServletRequest request, String requestId, HttpStatus status) { 
        try {
            var logMap = Map.of(
                    "timestamp", Instant.now().toString(),
                    "level", "ERROR",
                    "request_id", requestId,
                    "method", request.getMethod(),
                    "path", request.getRequestURI(),
                    "status", status.value(),
                    "error_message", ex.getMessage()
            );

            log.error(mapper.writeValueAsString(logMap), ex);
        } catch (Exception e) {
            log.error("Failed to log error properly", e);
        }
    }

}
