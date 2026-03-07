package com.todo.todo_api.controller;

import com.todo.todo_api.controler.HealthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HealthControllerTest {

    private MockMvc mockMvc;
    private HealthController healthController;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate = mock(JdbcTemplate.class);
        doNothing().when(jdbcTemplate).execute("SELECT 1");

        healthController = new HealthController(jdbcTemplate);

        mockMvc = MockMvcBuilders.standaloneSetup(healthController).build();
    }

    @Test
    void health_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk());
    }
}