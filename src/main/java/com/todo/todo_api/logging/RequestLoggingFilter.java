package com.todo.todo_api.logging;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
    throws ServletException, IOException {
        String requestId = UUID.randomUUID().toString();
        request.setAttribute("X-Request-Id", requestId);
        
        long startTime = System.currentTimeMillis();

        try{
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            var logMap = Map.of(
                "timestamp", Instant.now().toString(),
                "level", "info",
                "request_id", requestId,
                "method", request.getMethod(),
                "path", request.getRequestURI(),
                "status", response.getStatus(),
                "duration_ms", duration
            );

            log.info(mapper.writeValueAsString(logMap));
        }
    }
}
