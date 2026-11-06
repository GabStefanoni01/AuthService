package com.gabriel.authapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "AuthService API",
        version = "1.0.0",
        description = "API de autenticação e autorização com JWT, refresh tokens e recuperação de senha.",
        contact = @Contact(name = "AuthService")
    )
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Informe o access token JWT retornado pelo endpoint /auth/login."
)
public class OpenApiConfig {
}
