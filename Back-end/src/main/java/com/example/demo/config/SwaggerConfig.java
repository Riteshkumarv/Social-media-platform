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
        // Local backend (for testing)
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");

        // Production backend
        Server prodServer = new Server();
        prodServer.setUrl("https://backend-production-6085.up.railway.app");

        // Include both servers
        return new OpenAPI().servers(List.of(localServer, prodServer));
    }
}
