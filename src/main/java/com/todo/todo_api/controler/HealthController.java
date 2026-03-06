package com.todo.todo_api.controler;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HealthController {
    
    private final JdbcTemplate jdbcTemplate;
    private final Instant startTime = Instant.now();

    @GetMapping("/api/health")
    public ResponseEntity<Map<String, Object>> health() {
        String dbStatus = "healthy";
        long dbReponseTime = 0;

        try{
            Instant start = Instant.now();
            jdbcTemplate.execute("SELECT 1");
            dbReponseTime = Duration.between(start, Instant.now()).toMillis();
        } catch (Exception e) {
            dbStatus = "unhealthy";
        }

        Map<String, Object> response = Map.of(
            "status", dbStatus.equals("healthy") ? "healthy" : "unhealthy",
            "version", "1.0.0",
            "uptime_seconds", Duration.between(startTime, Instant.now()).getSeconds(),
            "checks", Map.of("database", Map.of(
                "status", dbStatus,
                "response_time_ms", dbReponseTime
            ))
        );

        return dbStatus.equals("healthy")
            ? ResponseEntity.ok(response)
            : ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);

    }
}
