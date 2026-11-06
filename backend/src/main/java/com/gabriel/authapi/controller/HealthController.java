package com.gabriel.authapi.controller;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health", description = "Verificação pública de disponibilidade da API.")
public class HealthController {

    @GetMapping("/")
    @Operation(summary = "Informações do serviço", description = "Retorna os links úteis do AuthService.")
    public Map<String, String> root() {
        return Map.of(
            "service", "AuthService",
            "status", "UP",
            "health", "/health",
            "documentation", "/swagger-ui/index.html"
        );
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Indica se a aplicação está respondendo.")
    public Map<String, String> health() {
        return Map.of(
            "status", "UP",
            "service", "AuthService"
        );
    }
}
