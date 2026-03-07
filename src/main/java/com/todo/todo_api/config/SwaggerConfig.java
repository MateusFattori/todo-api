package com.todo.todo_api.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SwaggerConfig {

    @EventListener(ApplicationReadyEvent.class)
    public void openSwagger() {
        try {
            String url = "http://localhost:8080/swagger-ui/index.html";
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
