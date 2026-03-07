package com.todo.todo_api.dtos.response;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;

import com.todo.todo_api.dto.response.PaginatedResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaginatedResponseTests {

    @Test
    void fromPage_shouldMapCorrectly() {
        List<String> content = List.of("A", "B");
        Pageable pageable = PageRequest.of(0, 2);
        Page<String> page = new PageImpl<>(content, pageable, 2);

        PaginatedResponse<String> response = PaginatedResponse.from(page);

        assertEquals(2, response.data().size());
        assertEquals(1, response.pagination().page());
        assertEquals(2, response.pagination().limit());
        assertEquals(1, response.pagination().total_pages());
    }
}