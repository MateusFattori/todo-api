package com.todo.todo_api.dto;

import org.apache.tomcat.util.http.parser.Priority;

public class CreateTaskRequest {
    private String title;
    private String description;
    private Priority priority;
    private String dueDate;
}
