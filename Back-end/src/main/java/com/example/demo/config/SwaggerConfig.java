package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Use relative server so Swagger UI targets the current host (works in dev and prod)
        Server relativeServer = new Server();
        relativeServer.setUrl("/");

        return new OpenAPI().servers(List.of(relativeServer));
    }
}
