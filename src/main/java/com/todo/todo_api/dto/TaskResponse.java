package com.todo.todo_api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.tomcat.util.http.parser.Priority;

public class TaskResponse {
    private UUID id;
    private String title;
    private String description;
    private String status;
    private Priority priority;
    private LocalDateTime dueDate;
}
