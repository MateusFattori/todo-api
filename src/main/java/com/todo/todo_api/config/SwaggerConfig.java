package com.todo.todo_api.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.net.URI;

@Component
public class SwaggerConfig {

    @EventListener(ApplicationReadyEvent.class)
    public void openSwagger() {
        try {
            String url = "http://localhost:8080/api/docs";

            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI(url));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}