package com.todo.todo_api.dto.request;

import java.time.LocalDateTime;

import com.todo.todo_api.domain.Priority;
import com.todo.todo_api.domain.Status;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {

    @NotBlank(message = "Title é obrigatório")
    @Size(min = 3, max = 255, message = "Title deve ter entre 3 e 255 caracteres")
    private String title;

    @Size(max = 1000, message = "Description deve ter no máximo 1000 caracteres")
    private String description;
    private Status status;
    private Priority priority;

    @Future(message = "Due date deve ser uma data futura")
    private LocalDateTime dueDate;
}
